package com.ctcse.ms.edumarket.core.payment.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceAlreadyExistsException;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.payment.dto.CreatePaymentRequest;
import com.ctcse.ms.edumarket.core.payment.dto.PaymentDto;
import com.ctcse.ms.edumarket.core.payment.dto.UpdatePaymentRequest;
import com.ctcse.ms.edumarket.core.payment.entity.PaymentEntity;
import com.ctcse.ms.edumarket.core.payment.repository.PaymentRepository;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.repository.PaymentScheduleRepository;
import com.ctcse.ms.edumarket.core.paymentSchedule.service.PaymentScheduleService;
import com.ctcse.ms.edumarket.core.paymentType.dto.PaymentTypeDto;
import com.ctcse.ms.edumarket.core.paymentType.entity.PaymentTypeEntity;
import com.ctcse.ms.edumarket.core.paymentType.repository.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final PaymentScheduleService paymentScheduleService;
    private final InstallmentStatusRepository installmentStatusRepository;

    @Transactional(readOnly = true)
    public List<PaymentDto> findAll() {
        List<PaymentEntity> entities = paymentRepository.findAllByActiveTrue();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PaymentDto convertToDto(PaymentEntity entity) {
        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setPaymentAmount(entity.getPaymentAmount());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setActive(entity.isActive());

        if (entity.getPaymentType() != null) {
            var payTypeDto = new PaymentTypeDto();
            payTypeDto.setId(entity.getPaymentType().getId());
            payTypeDto.setDescription(entity.getPaymentType().getDescription());
            dto.setPaymentType(payTypeDto);
        }

        if (entity.getPaymentSchedule() != null) {
            dto.setPaymentSchedule(paymentScheduleService.convertToDto(entity.getPaymentSchedule()));
        }

        return dto;
    }

    @Transactional
    public PaymentDto create(CreatePaymentRequest request) {
        PaymentTypeEntity paymentTypeEntity = paymentTypeRepository.findById(request.getIdPaymentType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de pago con id " + request.getIdPaymentType() + " no fue encontrado."));

        PaymentScheduleEntity paymentScheduleEntity = paymentScheduleRepository.findById(request.getIdPaymentSchedule())
                .orElseThrow(() -> new ResourceNotFoundException("El cronograma de pago con id " + request.getIdPaymentSchedule() + " no fue encontrado."));

        // --- VALIDACIÓN DE PAGO DUPLICADO ---
        Long currentStatusId = paymentScheduleEntity.getInstallmentStatus().getId();
        if (paymentScheduleEntity.getInstallmentStatus().getId() == 2L) {
            throw new ResourceAlreadyExistsException("La cuota con id " + request.getIdPaymentSchedule() + " ya ha sido pagada.");
        }

        InstallmentStatusEntity paidStatus = installmentStatusRepository.findById(2L) // Estado 'Pagado'
                .orElseThrow(() -> new ResourceNotFoundException("El estado 'Pagado' no se encontró en la base de datos."));

        paymentScheduleEntity.setInstallmentStatus(paidStatus);
        paymentScheduleRepository.save(paymentScheduleEntity);

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentAmount(paymentScheduleEntity.getInstallmentAmount());
        paymentEntity.setPaymentDate(request.getPaymentDate());
        paymentEntity.setPaymentType(paymentTypeEntity);
        paymentEntity.setPaymentSchedule(paymentScheduleEntity);

        paymentRepository.save(paymentEntity);

        // --- CONSTRUCCIÓN DE LA RESPUESTA SIMPLIFICADA ---
        PaymentDto confirmationResponse = new PaymentDto();

        confirmationResponse.setEnrollmentId(paymentScheduleEntity.getEnrollment().getId());
        confirmationResponse.setPaymentDate(paymentEntity.getPaymentDate());

        PaymentTypeDto paymentTypeDto = new PaymentTypeDto();
        paymentTypeDto.setId(paymentTypeEntity.getId());
        paymentTypeDto.setDescription(paymentTypeEntity.getDescription());
        confirmationResponse.setPaymentType(paymentTypeDto);

        InstallmentStatusDto statusDto = new InstallmentStatusDto();
        statusDto.setId(paidStatus.getId());
        statusDto.setStatus(paidStatus.getStatus());
        confirmationResponse.setInstallmentStatus(statusDto);

        // --- LÓGICA PARA NUMERAR LA CUOTA ---
        ConceptTypeDto conceptTypeDto = new ConceptTypeDto();
        conceptTypeDto.setId(paymentScheduleEntity.getConceptType().getId());
        String conceptDescription = paymentScheduleEntity.getConceptType().getDescription();

        // Si es una cuota mensual, calculamos su número
        if (conceptTypeDto.getId() == 2L) { // 2L es 'Cuota mensual'
            List<PaymentScheduleEntity> monthlyInstallments = paymentScheduleRepository.findByEnrollmentId(paymentScheduleEntity.getEnrollment().getId())
                    .stream()
                    .filter(schedule -> schedule.getConceptType().getId() == 2L)
                    .sorted(Comparator.comparing(PaymentScheduleEntity::getInstallmentDueDate))
                    .collect(Collectors.toList());

            int installmentNumber = -1;
            for (int i = 0; i < monthlyInstallments.size(); i++) {
                if (monthlyInstallments.get(i).getId().equals(paymentScheduleEntity.getId())) {
                    installmentNumber = i + 1;
                    break;
                }
            }

            if (installmentNumber != -1) {
                conceptDescription = "Cuota " + installmentNumber;
            }
        }

        conceptTypeDto.setDescription(conceptDescription);
        confirmationResponse.setConceptType(conceptTypeDto);

        confirmationResponse.setMessage("Pago registrado exitosamente.");

        return confirmationResponse;
    }

    @Transactional
    public PaymentDto update(Long id, UpdatePaymentRequest request) {
        PaymentEntity paymentEntity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El pago con id " + id + " no fue encontrado."));

        PaymentTypeEntity paymentTypeEntity = paymentTypeRepository.findById(request.getIdPaymentType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de pago con id " + request.getIdPaymentType() + " no fue encontrado."));

        PaymentScheduleEntity paymentScheduleEntity = paymentScheduleRepository.findById(request.getIdPaymentSchedule())
                .orElseThrow(() -> new ResourceNotFoundException("El cronograma de pago con id " + request.getIdPaymentSchedule() + " no fue encontrado."));

        paymentEntity.setPaymentAmount(request.getPaymentAmount());
        paymentEntity.setPaymentDate(request.getPaymentDate());
        paymentEntity.setPaymentType(paymentTypeEntity);
        paymentEntity.setPaymentSchedule(paymentScheduleEntity);

        PaymentEntity updatedEntity = paymentRepository.save(paymentEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public PaymentDto findById(Long id) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El pago con id " + id + " no fue encontrado."));
        return convertToDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("El pago con id " + id + " no fue encontrado.");
        }
        paymentRepository.deleteById(id);
    }

    @Transactional
    public void activateById(Long id) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + id));
        payment.setActive(true);
        paymentRepository.save(payment);
    }
}

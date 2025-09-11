package com.ctcse.ms.edumarket.core.paymentSchedule.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.service.ConceptTypeService;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.repository.ConceptTypeRepository;
import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import com.ctcse.ms.edumarket.core.enrollment.repository.EnrollmentRepository;
import com.ctcse.ms.edumarket.core.installmentStatus.service.InstallmentStatusService;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.payment.entity.PaymentEntity;
import com.ctcse.ms.edumarket.core.payment.repository.PaymentRepository;
import com.ctcse.ms.edumarket.core.payment.service.PaymentService;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.CreatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.PaymentScheduleDto;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.UpdatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.repository.PaymentScheduleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentScheduleService {

    // --- DEPENDENCIAS EXISTENTES ---
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final ConceptTypeRepository conceptTypeRepository;
    private final InstallmentStatusRepository installmentStatusRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ConceptTypeService conceptTypeService;
    private final InstallmentStatusService installmentStatusService;

    // --- NUEVAS DEPENDENCIAS PARA LA CASCADA DE ACTIVACIÓN ---
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    // --- CONSTRUCTOR EXPLÍCITO REEMPLAZANDO A @RequiredArgsConstructor ---
    public PaymentScheduleService(
            PaymentScheduleRepository paymentScheduleRepository,
            ConceptTypeRepository conceptTypeRepository,
            InstallmentStatusRepository installmentStatusRepository,
            EnrollmentRepository enrollmentRepository,
            ConceptTypeService conceptTypeService,
            InstallmentStatusService installmentStatusService,
            PaymentRepository paymentRepository,
            @Lazy PaymentService paymentService // @Lazy para prevenir ciclos
    ) {
        this.paymentScheduleRepository = paymentScheduleRepository;
        this.conceptTypeRepository = conceptTypeRepository;
        this.installmentStatusRepository = installmentStatusRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.conceptTypeService = conceptTypeService;
        this.installmentStatusService = installmentStatusService;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }


    // --- MÉTODO NUEVO PARA LA CASCADA DE ACTIVACIÓN ---
    @Transactional
    public void activateById(Long id) {
        PaymentScheduleEntity schedule = paymentScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cronograma no encontrado con id " + id));
        schedule.setActive(true);
        paymentScheduleRepository.save(schedule);

        List<PaymentEntity> payments = paymentRepository.findAllByPaymentScheduleIdAndActiveFalse(id);
        for (PaymentEntity payment : payments) {
            paymentService.activateById(payment.getId());
        }
    }

    // --- MÉTODO PARA DESACTIVACIÓN (BORRADO LÓGICO) ---
    @Transactional
    public void deactivateById(Long id) {
        PaymentScheduleEntity schedule = paymentScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cronograma de pago con id " + id + " no fue encontrado."));
        schedule.setActive(false);
        paymentScheduleRepository.save(schedule);
        // Aquí podrías continuar la cascada para desactivar los pagos si es necesario.
    }


    // ===============================================================
    // --- TUS MÉTODOS EXISTENTES (SIN CAMBIOS) ---
    // ===============================================================

    @Transactional(readOnly = true)
    public List<PaymentScheduleDto> findAll() {
        List<PaymentScheduleEntity> entities = paymentScheduleRepository.findAllByActiveTrue();
        Map<EnrollmentEntity, List<PaymentScheduleEntity>> groupedByEnrollment = entities.stream()
                .collect(Collectors.groupingBy(PaymentScheduleEntity::getEnrollment));

        List<PaymentScheduleDto> finalDtos = new ArrayList<>();

        for (List<PaymentScheduleEntity> schedulesForOneEnrollment : groupedByEnrollment.values()) {
            schedulesForOneEnrollment.sort(Comparator.comparing(PaymentScheduleEntity::getInstallmentDueDate));
            int monthlyInstallmentCounter = 0;
            for (PaymentScheduleEntity entity : schedulesForOneEnrollment) {
                PaymentScheduleDto dto = convertToDto(entity);
                if (dto.getConceptType() != null && dto.getConceptType().getId() == 2L) {
                    monthlyInstallmentCounter++;
                    dto.getConceptType().setDescription("Cuota " + monthlyInstallmentCounter);
                }
                finalDtos.add(dto);
            }
        }
        return finalDtos;
    }

    public PaymentScheduleDto convertToDto(PaymentScheduleEntity entity) {
        PaymentScheduleDto dto = new PaymentScheduleDto();
        dto.setId(entity.getId());
        dto.setInstallmentAmount(entity.getInstallmentAmount());
        dto.setInstallmentDueDate(entity.getInstallmentDueDate());
        dto.setActive(entity.isActive());

        if (entity.getConceptType() != null) {
            dto.setConceptType(conceptTypeService.convertToDto(entity.getConceptType()));
        }

        if (entity.getInstallmentStatus() != null) {
            dto.setInstallmentStatus(installmentStatusService.convertToDto(entity.getInstallmentStatus()));
        }

        return dto;
    }

    @Transactional
    public PaymentScheduleDto create(CreatePaymentScheduleRequest request) {
        ConceptTypeEntity conceptTypeEntity = conceptTypeRepository.findById(request.getIdConceptType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de concepto con id " + request.getIdConceptType() + " no fue encontrado"));

        InstallmentStatusEntity installmentStatusEntity = installmentStatusRepository.findById(request.getIdInstallmentStatus())
                .orElseThrow(() -> new ResourceNotFoundException("El estado de la cuota con id " + request.getIdInstallmentStatus() + "no fue encontrado"));

        EnrollmentEntity enrollmentEntity = enrollmentRepository.findById(request.getIdEnrollment())
                .orElseThrow(() -> new ResourceNotFoundException("La matrícula con id " + request.getIdEnrollment() + " no fue encontrada"));

        PaymentScheduleEntity paymentScheduleEntity = new PaymentScheduleEntity();
        paymentScheduleEntity.setInstallmentAmount(request.getInstallmentAmount());
        paymentScheduleEntity.setInstallmentDueDate(request.getInstallmentDueDate());
        paymentScheduleEntity.setConceptType(conceptTypeEntity);
        paymentScheduleEntity.setInstallmentStatus(installmentStatusEntity);
        paymentScheduleEntity.setEnrollment(enrollmentEntity);

        PaymentScheduleEntity savedPaymentScheduleEntity = paymentScheduleRepository.save(paymentScheduleEntity);

        return convertToDto(savedPaymentScheduleEntity);
    }

    @Transactional
    public PaymentScheduleDto update(Long id, UpdatePaymentScheduleRequest request) {
        PaymentScheduleEntity scheduleEntity = paymentScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cronograma de pago con id " + id + " no fue encontrado."));

        ConceptTypeEntity conceptTypeEntity = conceptTypeRepository.findById(request.getIdConceptType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de concepto con id " + request.getIdConceptType() + " no fue encontrado"));

        InstallmentStatusEntity installmentStatusEntity = installmentStatusRepository.findById(request.getIdInstallmentStatus())
                .orElseThrow(() -> new ResourceNotFoundException("El estado de la cuota con id " + request.getIdInstallmentStatus() + " no fue encontrado"));

        EnrollmentEntity enrollmentEntity = enrollmentRepository.findById(request.getIdEnrollment())
                .orElseThrow(() -> new ResourceNotFoundException("La matrícula con id " + request.getIdEnrollment() + " no fue encontrada"));

        scheduleEntity.setInstallmentAmount(request.getInstallmentAmount());
        scheduleEntity.setInstallmentDueDate(request.getInstallmentDueDate());
        scheduleEntity.setConceptType(conceptTypeEntity);
        scheduleEntity.setInstallmentStatus(installmentStatusEntity);
        scheduleEntity.setEnrollment(enrollmentEntity);

        PaymentScheduleEntity updatedEntity = paymentScheduleRepository.save(scheduleEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public PaymentScheduleDto findById(Long id) {
        PaymentScheduleEntity entity = paymentScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cronograma de pago con id " + id + " no fue encontrado."));
        return convertToDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!paymentScheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("El cronograma de pago con id " + id + " no fue encontrado.");
        }
        paymentScheduleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PaymentScheduleDto> findByEnrollmentId(Long enrollmentId) {
        List<PaymentScheduleEntity> schedules = paymentScheduleRepository.findByEnrollmentId(enrollmentId);

        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No se encontró un cronograma de pagos para la matrícula con id " + enrollmentId);
        }

        schedules.sort(Comparator.comparing(PaymentScheduleEntity::getInstallmentDueDate));

        List<PaymentScheduleDto> finalDtos = new ArrayList<>();
        int monthlyInstallmentCounter = 0;

        for (PaymentScheduleEntity entity : schedules) {
            PaymentScheduleDto dto = convertToDto(entity);

            if (dto.getConceptType() != null && dto.getConceptType().getId() == 2L) {
                monthlyInstallmentCounter++;
                dto.getConceptType().setDescription("Cuota " + monthlyInstallmentCounter);
            }
            finalDtos.add(dto);
        }

        return finalDtos;
    }
}


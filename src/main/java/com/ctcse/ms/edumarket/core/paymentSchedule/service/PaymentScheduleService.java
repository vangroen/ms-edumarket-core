package com.ctcse.ms.edumarket.core.paymentSchedule.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.service.ConceptTypeService;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.repository.ConceptTypeRepository;
import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import com.ctcse.ms.edumarket.core.enrollment.service.EnrollmentService;
import com.ctcse.ms.edumarket.core.enrollment.repository.EnrollmentRepository;
import com.ctcse.ms.edumarket.core.installmentStatus.service.InstallmentStatusService;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.CreatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.PaymentScheduleDto;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.UpdatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.repository.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final ConceptTypeRepository conceptTypeRepository;
    private final InstallmentStatusRepository installmentStatusRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;
    private final ConceptTypeService conceptTypeService;
    private final InstallmentStatusService installmentStatusService;

    @Transactional(readOnly = true)
    public List<PaymentScheduleDto> findAll() {
        List<PaymentScheduleEntity> entities = paymentScheduleRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PaymentScheduleDto convertToDto(PaymentScheduleEntity entity) {
        PaymentScheduleDto dto = new PaymentScheduleDto();
        dto.setId(entity.getId());
        dto.setInstallmentAmount(entity.getInstallmentAmount());
        dto.setInstallmentDueDate(entity.getInstallmentDueDate());

        if (entity.getConceptType() != null) {
            dto.setConceptType(conceptTypeService.convertToDto(entity.getConceptType()));
        }

        if (entity.getInstallmentStatus() != null) {
            dto.setInstallmentStatus(installmentStatusService.convertToDto(entity.getInstallmentStatus()));
        }

        if (entity.getEnrollment() != null) {
            dto.setEnrollment(enrollmentService.convertToDto(entity.getEnrollment())); // Delegar la conversión
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
}

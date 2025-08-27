package com.ctcse.ms.edumarket.core.paymentSchedule.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.repository.ConceptTypeRepository;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.CreatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.PaymentScheduleDto;
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

    @Transactional(readOnly = true)
    public List<PaymentScheduleDto> findAll() {
        List<PaymentScheduleEntity> entities = paymentScheduleRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PaymentScheduleDto convertToDto(PaymentScheduleEntity entity) {
        PaymentScheduleDto dto = new PaymentScheduleDto();
        dto.setId(entity.getId());
        dto.setInstallmentAmount(entity.getInstallmentAmount());
        dto.setInstallmentDueDate(entity.getInstallmentDueDate());

        if (entity.getConceptType() != null) {
            var conceptTypeDto = new ConceptTypeDto();
            conceptTypeDto.setId(entity.getConceptType().getId());
            conceptTypeDto.setDescription(entity.getConceptType().getDescription());
            dto.setConceptType(conceptTypeDto);
        }

        if (entity.getInstallmentStatus() != null) {
            var installmentStatusDto = new InstallmentStatusDto();
            installmentStatusDto.setId(entity.getInstallmentStatus().getId());
            installmentStatusDto.setStatus(entity.getInstallmentStatus().getStatus());
            dto.setInstallmentStatus(installmentStatusDto);
        }

        return dto;
    }

    @Transactional
    public PaymentScheduleDto create(CreatePaymentScheduleRequest request) {
        ConceptTypeEntity conceptTypeEntity = conceptTypeRepository.findById(request.getIdConceptType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de concepto con id " + request.getIdConceptType() + " no fue encontrado"));

        InstallmentStatusEntity installmentStatusEntity = installmentStatusRepository.findById(request.getIdInstallmentStatus())
                .orElseThrow(() -> new ResourceNotFoundException("El estado de la cuota con id " + request.getIdInstallmentStatus() + "no fue encontrado"));

        PaymentScheduleEntity paymentScheduleEntity = new PaymentScheduleEntity();
        paymentScheduleEntity.setInstallmentAmount(request.getInstallmentAmount());
        paymentScheduleEntity.setInstallmentDueDate(request.getInstallmentDueDate());
        paymentScheduleEntity.setConceptType(conceptTypeEntity);
        paymentScheduleEntity.setInstallmentStatus(installmentStatusEntity);

        PaymentScheduleEntity savedPaymentScheduleEntity = paymentScheduleRepository.save(paymentScheduleEntity);

        return convertToDto(paymentScheduleEntity);
    }
}

package com.ctcse.ms.edumarket.core.payment.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.payment.dto.CreatePaymentRequest;
import com.ctcse.ms.edumarket.core.payment.dto.PaymentDto;
import com.ctcse.ms.edumarket.core.payment.entity.PaymentEntity;
import com.ctcse.ms.edumarket.core.payment.repository.PaymentRepository;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.PaymentScheduleDto;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.repository.PaymentScheduleRepository;
import com.ctcse.ms.edumarket.core.paymentType.dto.PaymentTypeDto;
import com.ctcse.ms.edumarket.core.paymentType.entity.PaymentTypeEntity;
import com.ctcse.ms.edumarket.core.paymentType.repository.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;

    @Transactional(readOnly = true)
    public List<PaymentDto> findAll() {
        List<PaymentEntity> entities = paymentRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PaymentDto convertToDto(PaymentEntity entity) {
        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setPaymentAmount(entity.getPaymentAmount());
        dto.setPaymentDate(entity.getPaymentDate());

        if (entity.getPaymentType() != null) {
            var payTypeDto = new PaymentTypeDto();
            payTypeDto.setId(entity.getPaymentType().getId());
            payTypeDto.setDescription(entity.getPaymentType().getDescription());
            dto.setPaymentType(payTypeDto);
        }

        return dto;
    }

    private PaymentScheduleDto getPaymentScheduleDto(PaymentScheduleEntity paymentSchedule) {
        var paymentScheduleDto = new PaymentScheduleDto();
        paymentScheduleDto.setId(paymentSchedule.getId());
        paymentScheduleDto.setInstallmentAmount(paymentSchedule.getInstallmentAmount());
        paymentScheduleDto.setInstallmentDueDate(paymentSchedule.getInstallmentDueDate());

        if (paymentSchedule.getConceptType() != null) {
            var conceptType = paymentSchedule.getConceptType();
            var conceptTypeDto = new ConceptTypeDto();
            conceptTypeDto.setId(conceptType.getId());
            conceptTypeDto.setDescription(conceptType.getDescription());

            paymentScheduleDto.setConceptType(conceptTypeDto);
        }

        if (paymentSchedule.getInstallmentStatus() != null) {
            var installmentStatus = paymentSchedule.getInstallmentStatus();
            var installmentStatusDto = new InstallmentStatusDto();
            installmentStatusDto.setId(installmentStatus.getId());
            installmentStatusDto.setStatus(installmentStatus.getStatus());

            paymentScheduleDto.setInstallmentStatus(installmentStatusDto);
        }

        return paymentScheduleDto;
    }

    @Transactional
    public PaymentDto create(CreatePaymentRequest request) {
        PaymentTypeEntity paymentTypeEntity = paymentTypeRepository.findById(request.getIdPaymentType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de documento con id " + request.getIdPaymentType() + " no fue encontrado."));


        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentAmount(request.getPaymentAmount());
        paymentEntity.setPaymentDate(request.getPaymentDate());
        paymentEntity.setPaymentType(paymentTypeEntity);

        PaymentEntity savedPaymentEntity = paymentRepository.save(paymentEntity);
        return convertToDto(savedPaymentEntity);
    }
}

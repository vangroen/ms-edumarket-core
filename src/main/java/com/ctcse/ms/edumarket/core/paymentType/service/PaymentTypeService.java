package com.ctcse.ms.edumarket.core.paymentType.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.paymentType.dto.CreatePaymentTypeRequest;
import com.ctcse.ms.edumarket.core.paymentType.dto.PaymentTypeDto;
import com.ctcse.ms.edumarket.core.paymentType.dto.UpdatePaymentTypeRequest;
import com.ctcse.ms.edumarket.core.paymentType.entity.PaymentTypeEntity;
import com.ctcse.ms.edumarket.core.paymentType.repository.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository repository;

    public List<PaymentTypeDto> findAll() {
        List<PaymentTypeEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PaymentTypeDto convertToDto(PaymentTypeEntity entity) {
        PaymentTypeDto dto = new PaymentTypeDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public PaymentTypeDto create(CreatePaymentTypeRequest request) {
        PaymentTypeEntity entity = new PaymentTypeEntity();
        entity.setDescription(request.getDescription());

        PaymentTypeEntity saveEntity = repository.save(entity);
        return convertToDto(saveEntity);
    }

    @Transactional
    public PaymentTypeDto update(Long id, UpdatePaymentTypeRequest request) {
        PaymentTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de pago con id " + id + " no fue encontrado."));

        entity.setDescription(request.getDescription());
        PaymentTypeEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }
}

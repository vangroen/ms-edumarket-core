package com.ctcse.ms.edumarket.core.installmentStatus.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.CreateInstallmentStatusRequest;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.UpdateInstallmentStatusRequest;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.institutionType.dto.CreateInstitutionTypeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstallmentStatusService {

    private final InstallmentStatusRepository repository;

    public List<InstallmentStatusDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InstallmentStatusDto convertToDto(InstallmentStatusEntity entity) {
        InstallmentStatusDto dto = new InstallmentStatusDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public InstallmentStatusDto create(CreateInstallmentStatusRequest request) {
        InstallmentStatusEntity entity = new InstallmentStatusEntity();
        entity.setStatus(request.getStatus());
        InstallmentStatusEntity savedEntity = repository.save(entity);
        return convertToDto(savedEntity);
    }

    @Transactional
    public InstallmentStatusDto update(Long id, UpdateInstallmentStatusRequest request) {
        InstallmentStatusEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El estado de cuota con id " + id + " no fue encontrado."));

        entity.setStatus(request.getStatus());
        InstallmentStatusEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }
}

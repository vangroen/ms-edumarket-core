package com.ctcse.ms.edumarket.core.installmentStatus.service;

import com.ctcse.ms.edumarket.core.installmentStatus.dto.CreateInstallmentStatusRequest;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.institutionType.dto.CreateInstitutionTypeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private InstallmentStatusDto convertToDto(InstallmentStatusEntity entity) {
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
}

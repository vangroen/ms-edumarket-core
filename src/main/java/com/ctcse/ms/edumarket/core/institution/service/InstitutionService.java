package com.ctcse.ms.edumarket.core.institution.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.institution.dto.CreateInstitutionRequest;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.institution.repository.InstitutionRepository;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.institutionType.entity.InstitutionTypeEntity;
import com.ctcse.ms.edumarket.core.institutionType.repository.InstitutionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionTypeRepository institutionTypeRepository;

    @Transactional(readOnly = true)
    public List<InstitutionDto> findAll() {
        List<InstitutionEntity> entities = institutionRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InstitutionDto convertToDto(InstitutionEntity entity) {
        InstitutionDto dto = new InstitutionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        if (entity.getInstitutionType() != null) {
            var institutionTypeDto = new InstitutionTypeDto();
            institutionTypeDto.setId(entity.getInstitutionType().getId());
            institutionTypeDto.setDescription(entity.getInstitutionType().getDescription());
            dto.setInstitutionType(institutionTypeDto);
        }

        return dto;
    }

    @Transactional
    public InstitutionDto create(CreateInstitutionRequest request) {
        InstitutionTypeEntity institutionTypeEntity = institutionTypeRepository.findById(request.getIdInstitutionType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de institución con id " + request.getIdInstitutionType() + " no fue encontrado."));

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setName(request.getName());
        institutionEntity.setInstitutionType(institutionTypeEntity);

        InstitutionEntity savedInstitutionEntity = institutionRepository.save(institutionEntity);
        return convertToDto(savedInstitutionEntity);
    }

}

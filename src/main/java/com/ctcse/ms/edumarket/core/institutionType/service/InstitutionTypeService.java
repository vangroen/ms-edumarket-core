package com.ctcse.ms.edumarket.core.institutionType.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.institutionType.dto.CreateInstitutionTypeRequest;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.institutionType.dto.UpdateInstitutionTypeRequest;
import com.ctcse.ms.edumarket.core.institutionType.entity.InstitutionTypeEntity;
import com.ctcse.ms.edumarket.core.institutionType.repository.InstitutionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionTypeService {

    private final InstitutionTypeRepository repository;

    public List<InstitutionTypeDto> findAll() {
        List<InstitutionTypeEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InstitutionTypeDto convertToDto(InstitutionTypeEntity entity) {
        InstitutionTypeDto dto = new InstitutionTypeDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public InstitutionTypeDto create(CreateInstitutionTypeRequest request) {
        InstitutionTypeEntity entity = new InstitutionTypeEntity();
        entity.setDescription(request.getDescription());

        InstitutionTypeEntity saveEntity = repository.save(entity);
        return convertToDto(saveEntity);
    }

    @Transactional
    public InstitutionTypeDto update(Long id, UpdateInstitutionTypeRequest request) {
        InstitutionTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de institución con id " + id + " no fue encontrado."));
        entity.setDescription(request.getDescription());
        InstitutionTypeEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }
}

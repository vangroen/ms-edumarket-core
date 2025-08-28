package com.ctcse.ms.edumarket.core.conceptType.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.conceptType.dto.CreateConceptTypeRequest;
import com.ctcse.ms.edumarket.core.conceptType.dto.UpdateConceptTypeRequest;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.repository.ConceptTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConceptTypeService {

    private final ConceptTypeRepository repository;

    public List<ConceptTypeDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ConceptTypeDto convertToDto(ConceptTypeEntity entity) {
        ConceptTypeDto dto = new ConceptTypeDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public ConceptTypeDto create(CreateConceptTypeRequest request) {
        ConceptTypeEntity entity = new ConceptTypeEntity();
        entity.setDescription(request.getDescription());

        ConceptTypeEntity saveEntity = repository.save(entity);
        return convertToDto(saveEntity);
    }

    @Transactional
    public ConceptTypeDto update(Long id, UpdateConceptTypeRequest request) {
        ConceptTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de concepto con id " + id + " no fue encontrado."));

        entity.setDescription(request.getDescription());
        ConceptTypeEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }
}

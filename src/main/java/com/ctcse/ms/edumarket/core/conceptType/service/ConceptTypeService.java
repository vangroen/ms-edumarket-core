package com.ctcse.ms.edumarket.core.conceptType.service;

import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.conceptType.dto.CreateConceptTypeRequest;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.repository.ConceptTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

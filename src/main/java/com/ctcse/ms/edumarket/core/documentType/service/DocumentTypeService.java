package com.ctcse.ms.edumarket.core.documentType.service;

import com.ctcse.ms.edumarket.core.documentType.dto.CreateDocumentTypeRequest;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.documentType.entity.DocumentTypeEntity;
import com.ctcse.ms.edumarket.core.documentType.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository repository;

    public List<DocumentTypeDto> findAll() {
        List<DocumentTypeEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DocumentTypeDto convertToDto(DocumentTypeEntity entity) {
        DocumentTypeDto dto = new DocumentTypeDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public DocumentTypeDto create(CreateDocumentTypeRequest request) {
        DocumentTypeEntity entity = new DocumentTypeEntity();
        entity.setDescription(request.getDescription());

        DocumentTypeEntity saveEntity = repository.save(entity);
        return convertToDto(saveEntity);
    }
}

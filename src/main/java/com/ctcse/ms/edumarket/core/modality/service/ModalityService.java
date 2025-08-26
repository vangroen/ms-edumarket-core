package com.ctcse.ms.edumarket.core.modality.service;



import com.ctcse.ms.edumarket.core.modality.dto.CreateModalityRequest;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import com.ctcse.ms.edumarket.core.modality.repository.ModalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModalityService {

    private final ModalityRepository repository;

    public List<ModalityDto> findAll() {
        List<ModalityEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ModalityDto convertToDto(ModalityEntity entity) {
        ModalityDto dto = new ModalityDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public ModalityDto create(CreateModalityRequest request) {
        ModalityEntity entity = new ModalityEntity();
        entity.setDescription(request.getDescription());

        ModalityEntity saveEntity = repository.save(entity);
        return convertToDto(saveEntity);
    }
}

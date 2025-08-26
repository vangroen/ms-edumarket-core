package com.ctcse.ms.edumarket.core.profession.service;

import com.ctcse.ms.edumarket.core.profession.dto.CreateProfessionRequest;
import com.ctcse.ms.edumarket.core.profession.dto.ProfessionDto;
import com.ctcse.ms.edumarket.core.profession.entity.ProfessionEntity;
import com.ctcse.ms.edumarket.core.profession.repository.ProfessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionService {

    private final ProfessionRepository repository;

    public List<ProfessionDto> findAll() {
        List<ProfessionEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProfessionDto create(CreateProfessionRequest request) {
        ProfessionEntity entity = new ProfessionEntity();
        entity.setName(request.getName());

        ProfessionEntity savedEntity = repository.save(entity);
        return convertToDto(savedEntity);
    }

    private ProfessionDto convertToDto(ProfessionEntity entity) {
        ProfessionDto dto = new ProfessionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}

package com.ctcse.ms.edumarket.core.academicRank.service;

import com.ctcse.ms.edumarket.core.academicRank.dto.AcademicRankDto;
import com.ctcse.ms.edumarket.core.academicRank.dto.CreateAcademicRankRequest;
import com.ctcse.ms.edumarket.core.academicRank.dto.UpdateAcademicRankRequest;
import com.ctcse.ms.edumarket.core.academicRank.entity.AcademicRankEntity;
import com.ctcse.ms.edumarket.core.academicRank.repository.AcademicRankRepository;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicRankService {

    private final AcademicRankRepository repository;

    public List<AcademicRankDto> findAll() {
        List<AcademicRankEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AcademicRankDto convertToDto(AcademicRankEntity entity) {
        AcademicRankDto dto = new AcademicRankDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public AcademicRankDto create(CreateAcademicRankRequest request) {
        AcademicRankEntity entity = new AcademicRankEntity();
        entity.setDescription(request.getDescription());

        AcademicRankEntity saveEntity = repository.save(entity);
        return convertToDto(saveEntity);
    }

    @Transactional
    public AcademicRankDto update(Long id, UpdateAcademicRankRequest request) {
        AcademicRankEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El rango académico con id " + id + " no fue encontrado."));

        entity.setDescription(request.getDescription());
        AcademicRankEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }
}

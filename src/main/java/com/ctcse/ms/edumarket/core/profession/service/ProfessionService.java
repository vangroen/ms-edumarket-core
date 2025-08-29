package com.ctcse.ms.edumarket.core.profession.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.profession.dto.CreateProfessionRequest;
import com.ctcse.ms.edumarket.core.profession.dto.ProfessionDto;
import com.ctcse.ms.edumarket.core.profession.dto.UpdateProfessionRequest;
import com.ctcse.ms.edumarket.core.profession.entity.ProfessionEntity;
import com.ctcse.ms.edumarket.core.profession.repository.ProfessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ProfessionDto update(Long id, UpdateProfessionRequest request) {
        ProfessionEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La profesión con id " + id + " no fue encontrada."));

        entity.setName(request.getName());
        ProfessionEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public ProfessionDto findById(Long id) {
        ProfessionEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La profesión con id " + id + " no fue encontrada."));
        return convertToDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("La profesión con id " + id + " no fue encontrada.");
        }
        repository.deleteById(id);
    }
}

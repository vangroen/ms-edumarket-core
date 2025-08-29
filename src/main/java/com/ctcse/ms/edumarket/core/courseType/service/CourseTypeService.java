package com.ctcse.ms.edumarket.core.courseType.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.dto.CreateCourseTypeRequest;
import com.ctcse.ms.edumarket.core.courseType.dto.UpdateCourseTypeRequest;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.courseType.repository.CourseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseTypeService {

    private final CourseTypeRepository repository;

    public List<CourseTypeDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CourseTypeDto convertToDto(CourseTypeEntity entity) {
        CourseTypeDto dto = new CourseTypeDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public CourseTypeDto create(CreateCourseTypeRequest request) {
        CourseTypeEntity entity = new CourseTypeEntity();
        entity.setDescription(request.getDescription());
        CourseTypeEntity savedEntity = repository.save(entity);
        return convertToDto(savedEntity);
    }

    @Transactional
    public CourseTypeDto update(Long id, UpdateCourseTypeRequest request) {
        CourseTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de curso con id " + id + " no fue encontrado."));

        entity.setDescription(request.getDescription());
        CourseTypeEntity updatedEntity = repository.save(entity);

        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public CourseTypeDto findById(Long id) {
        CourseTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de curso con id " + id + " no fue encontrado."));
        return convertToDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("El tipo de curso con id " + id + " no fue encontrado.");
        }
        repository.deleteById(id);
    }
}

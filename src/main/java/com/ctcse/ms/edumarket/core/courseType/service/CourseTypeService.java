package com.ctcse.ms.edumarket.core.courseType.service;

import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.dto.CreateCourseTypeRequest;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.courseType.repository.CourseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

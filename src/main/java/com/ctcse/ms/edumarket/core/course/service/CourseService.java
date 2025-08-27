package com.ctcse.ms.edumarket.core.course.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.course.dto.CreateCourseRequest;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.course.repository.CourseRepository;
import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.courseType.repository.CourseTypeRepository;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import com.ctcse.ms.edumarket.core.modality.repository.ModalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseTypeRepository courseTypeRepository;
    private final ModalityRepository modalityRepository;

    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        List<CourseEntity> entities = courseRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CourseDto convertToDto(CourseEntity entity) {
        CourseDto dto = new CourseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        if (entity.getCourseType() != null) {
            var courseTypeDto = new CourseTypeDto();
            courseTypeDto.setId(entity.getCourseType().getId());
            courseTypeDto.setDescription(entity.getCourseType().getDescription());
            dto.setCourseType(courseTypeDto);
        }

        if (entity.getModality() != null) {
            var modalityDto = new ModalityDto();
            modalityDto.setId(entity.getModality().getId());
            modalityDto.setDescription(entity.getModality().getDescription());
            dto.setModality(modalityDto);
        }

        return dto;
    }

    @Transactional
    public CourseDto create(CreateCourseRequest request) {
        CourseTypeEntity courseTypeEntity = courseTypeRepository.findById(request.getIdCourseType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de curso con id " + request.getIdCourseType() + " no fue encontrado"));

        ModalityEntity modalityEntity = modalityRepository.findById(request.getIdModality())
                .orElseThrow(() -> new ResourceNotFoundException("La modalidad con id " + request.getIdModality() + " no fue encontrado"));

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName(request.getName());
        courseEntity.setCourseType(courseTypeEntity);
        courseEntity.setModality(modalityEntity);

        CourseEntity savedCourseEntity = courseRepository.save(courseEntity);

        return convertToDto(savedCourseEntity);
    }
}

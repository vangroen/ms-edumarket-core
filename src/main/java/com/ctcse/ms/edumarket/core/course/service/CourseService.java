package com.ctcse.ms.edumarket.core.course.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.course.dto.CourseWithInstitutionsDto;
import com.ctcse.ms.edumarket.core.course.dto.CreateCourseRequest;
import com.ctcse.ms.edumarket.core.course.dto.UpdateCourseRequest;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.course.repository.CourseRepository;
import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.courseType.repository.CourseTypeRepository;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.institution.repository.InstitutionRepository;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import com.ctcse.ms.edumarket.core.modality.repository.ModalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseTypeRepository courseTypeRepository;
    private final ModalityRepository modalityRepository;
    private final InstitutionRepository institutionRepository;

    @Transactional(readOnly = true)
    public List<CourseWithInstitutionsDto> findAllCoursesWithInstitutions() {
        return courseRepository.findAll().stream()
                .map(this::convertToCourseWithInstitutionsDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        List<CourseEntity> entities = courseRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseDto convertToDto(CourseEntity entity) {
        CourseDto dto = new CourseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCourseCost(entity.getCourseCost());

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

        List<InstitutionDto> institutionDtos = entity.getInstitutions().stream()
                .map(this::getInstitutionDto)
                .collect(Collectors.toList());
        dto.setInstitutions(institutionDtos);

        return dto;
    }

    private InstitutionDto getInstitutionDto(InstitutionEntity institution) {
        var institutionDto = new InstitutionDto();
        institutionDto.setId(institution.getId());
        institutionDto.setName(institution.getName());

        if (institution.getInstitutionType() != null) {
            var institutionType = institution.getInstitutionType();
            var institutionTypeDto = new InstitutionTypeDto();
            institutionTypeDto.setId(institutionType.getId());
            institutionTypeDto.setDescription(institutionType.getDescription());

            institutionDto.setInstitutionType(institutionTypeDto);
        }

        return institutionDto;
    }

    @Transactional
    public CourseDto create(CreateCourseRequest request) {
        CourseTypeEntity courseTypeEntity = courseTypeRepository.findById(request.getIdCourseType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de curso con id " + request.getIdCourseType() + " no fue encontrado"));

        ModalityEntity modalityEntity = modalityRepository.findById(request.getIdModality())
                .orElseThrow(() -> new ResourceNotFoundException("La modalidad con id " + request.getIdModality() + " no fue encontrado"));

        Set<Long> uniqueInstitutionIds = new HashSet<>(request.getIdInstitutions());

        List<InstitutionEntity> institutions = institutionRepository.findAllById(uniqueInstitutionIds);
        if (institutions.size() != uniqueInstitutionIds.size()) {
            throw new ResourceNotFoundException("Una o más instituciones no fueron encontradas.");
        }

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName(request.getName());
        courseEntity.setCourseCost(request.getCourseCost());
        courseEntity.setCourseType(courseTypeEntity);
        courseEntity.setModality(modalityEntity);
        courseEntity.setInstitutions(new HashSet<>(institutions));

        CourseEntity savedCourseEntity = courseRepository.save(courseEntity);
        return convertToDto(savedCourseEntity);
    }

    @Transactional
    public CourseDto update(Long id, UpdateCourseRequest request) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El curso con id " + id + " no fue encontrado."));

        CourseTypeEntity courseTypeEntity = courseTypeRepository.findById(request.getIdCourseType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de curso con id " + request.getIdCourseType() + " no fue encontrado"));

        ModalityEntity modalityEntity = modalityRepository.findById(request.getIdModality())
                .orElseThrow(() -> new ResourceNotFoundException("La modalidad con id " + request.getIdModality() + " no fue encontrado"));

        Set<Long> uniqueInstitutionIds = new HashSet<>(request.getIdInstitutions());

        List<InstitutionEntity> institutions = institutionRepository.findAllById(uniqueInstitutionIds);
        if (institutions.size() != uniqueInstitutionIds.size()) {
            throw new ResourceNotFoundException("Una o más instituciones no fueron encontradas.");
        }

        courseEntity.setName(request.getName());
        courseEntity.setCourseCost(request.getCourseCost());
        courseEntity.setCourseType(courseTypeEntity);
        courseEntity.setModality(modalityEntity);
        courseEntity.getInstitutions().clear();
        courseEntity.getInstitutions().addAll(institutions);

        CourseEntity updatedEntity = courseRepository.save(courseEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public CourseDto findById(Long id) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El curso con id " + id + " no fue encontrado."));
        return convertToDto(courseEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("El curso con id " + id + " no fue encontrado.");
        }
        courseRepository.deleteById(id);
    }

    private CourseWithInstitutionsDto convertToCourseWithInstitutionsDto(CourseEntity entity) {
        CourseWithInstitutionsDto dto = new CourseWithInstitutionsDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setInstitutions(entity.getInstitutions().stream()
                .map(this::getInstitutionDto).collect(Collectors.toList()));
        return dto;
    }
}

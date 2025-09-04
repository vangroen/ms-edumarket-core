package com.ctcse.ms.edumarket.core.course.service;


import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.course.dto.CreateCourseRequest;
import com.ctcse.ms.edumarket.core.course.dto.UpdateCourseRequest;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.course.entity.CourseInstitutionEntity;
import com.ctcse.ms.edumarket.core.course.entity.CourseInstitutionId;
import com.ctcse.ms.edumarket.core.course.repository.CourseRepository;
import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.courseType.repository.CourseTypeRepository;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionPriceDto;
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
        dto.setDurationInMonths(entity.getDurationInMonths());


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

        List<InstitutionPriceDto> institutionDtos = entity.getInstitutions().stream()
                .map(courseInstitution -> {
                    InstitutionPriceDto institutionPriceDto = new InstitutionPriceDto();
                    institutionPriceDto.setPrice(courseInstitution.getPrice());
                    institutionPriceDto.setInstitution(getInstitutionDto(courseInstitution.getInstitution()));
                    return institutionPriceDto;
                })
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

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName(request.getName());
        courseEntity.setCourseType(courseTypeEntity);
        courseEntity.setModality(modalityEntity);
        courseEntity.setDurationInMonths(request.getDurationInMonths());

        // Guardamos el curso primero para obtener un ID
        CourseEntity savedCourseEntity = courseRepository.save(courseEntity);


        Set<CourseInstitutionEntity> courseInstitutions = new HashSet<>();
        for (InstitutionPriceDto inst : request.getInstitutions()) {
            InstitutionEntity institutionEntity = institutionRepository.findById(inst.getInstitution().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada"));

            CourseInstitutionEntity courseInstitution = new CourseInstitutionEntity();
            courseInstitution.setCourse(savedCourseEntity);
            courseInstitution.setInstitution(institutionEntity);
            courseInstitution.setPrice(inst.getPrice());
            courseInstitution.setId(new CourseInstitutionId(savedCourseEntity.getId(), institutionEntity.getId()));
            courseInstitutions.add(courseInstitution);
        }

        savedCourseEntity.setInstitutions(courseInstitutions);


        return convertToDto(courseRepository.save(savedCourseEntity));
    }

    @Transactional
    public CourseDto update(Long id, UpdateCourseRequest request) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El curso con id " + id + " no fue encontrado."));

        CourseTypeEntity courseTypeEntity = courseTypeRepository.findById(request.getIdCourseType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de curso con id " + request.getIdCourseType() + " no fue encontrado"));

        ModalityEntity modalityEntity = modalityRepository.findById(request.getIdModality())
                .orElseThrow(() -> new ResourceNotFoundException("La modalidad con id " + request.getIdModality() + " no fue encontrado"));

        courseEntity.setName(request.getName());
        courseEntity.setCourseType(courseTypeEntity);
        courseEntity.setModality(modalityEntity);

        courseEntity.getInstitutions().clear();

        Set<CourseInstitutionEntity> courseInstitutions = new HashSet<>();
        for (InstitutionPriceDto inst : request.getInstitutions()) {
            InstitutionEntity institutionEntity = institutionRepository.findById(inst.getInstitution().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada"));

            CourseInstitutionEntity courseInstitution = new CourseInstitutionEntity();
            courseInstitution.setCourse(courseEntity);
            courseInstitution.setInstitution(institutionEntity);
            courseInstitution.setPrice(inst.getPrice());
            courseInstitution.setId(new CourseInstitutionId(courseEntity.getId(), institutionEntity.getId()));
            courseInstitutions.add(courseInstitution);
        }

        courseEntity.getInstitutions().addAll(courseInstitutions);

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
}
package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseWithInstitutionsDto {

    private Long id;
    private String name;
    private BigDecimal courseCost;
    private CourseTypeDto courseType;
    private ModalityDto modality;
    private List<InstitutionDto> institutions;
}

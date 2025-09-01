package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import lombok.Data;

import java.util.List;

@Data
public class CourseWithInstitutionsDto {

    private Long id;
    private String name;
    private List<InstitutionDto> institutions;
}

package com.ctcse.ms.edumarket.core.institution.dto;

import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import lombok.Data;

@Data
public class InstitutionDto {

    private Long id;
    private String name;
    private InstitutionTypeDto institutionType;
}

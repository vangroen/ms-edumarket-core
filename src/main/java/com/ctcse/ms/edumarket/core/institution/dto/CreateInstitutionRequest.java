package com.ctcse.ms.edumarket.core.institution.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import lombok.Data;

@Data
public class CreateInstitutionRequest {

    @NotBlankWithMessage(fieldName = "nombre")
    @SizeWithMessage(fieldName = "nombre", min = 2, max = 100)
    private String name;

    @NotBlankWithMessage(fieldName = "id del tipo de insitituto")
    @SizeWithMessage(fieldName = "id del tipo de insitituto", min = 2, max = 100)
    private Long idInstitutionType;
}

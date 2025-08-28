package com.ctcse.ms.edumarket.core.institution.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateInstitutionRequest {

    @NotBlankWithMessage(fieldName = "nombre")
    @SizeWithMessage(fieldName = "nombre", min = 2, max = 100)
    private String name;

    @NotNull(message = "El ID del tipo de institución no puede ser nulo.")
    private Long idInstitutionType;
}
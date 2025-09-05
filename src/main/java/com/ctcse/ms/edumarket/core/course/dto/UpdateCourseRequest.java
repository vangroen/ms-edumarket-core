package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionPriceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateCourseRequest {

    @NotBlankWithMessage(fieldName = "nombre")
    @SizeWithMessage(fieldName = "nombre", min = 3, max = 100)
    private String name;

    @NotNull(message = "El ID del tipo de curso (idCourseType) no puede ser nulo.")
    private Long idCourseType;

    @NotNull(message = "El ID de la modalidad (idModality) no puede ser nulo.")
    private Long idModality;

    @NotEmpty(message = "Debe proporcionar al menos una institución con su precio y duración.")
    @Valid // Añadimos @Valid para que se validen los objetos dentro de la lista
    private List<InstitutionPriceDto> institutions;
}
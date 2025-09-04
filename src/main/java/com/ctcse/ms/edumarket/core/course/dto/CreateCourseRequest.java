package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionPriceDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateCourseRequest {

    @NotBlankWithMessage(fieldName = "nombre")
    @SizeWithMessage(fieldName = "nombre", min = 3, max = 100)
    private String name;

    @NotNull(message = "El ID del tipo de curso (idCourseType) no puede ser nulo.")
    private Long idCourseType;

    @NotNull(message = "El ID de la modalidad (idModality) no puede ser nulo.")
    private Long idModality;

    // --- CAMBIO CLAVE 1: Añadir el campo de duración ---
    @NotNull(message = "La duración en meses no puede ser nula.")
    @Min(value = 1, message = "La duración debe ser de al menos 1 mes.")
    private Integer durationInMonths;

    @NotEmpty(message = "Debe proporcionar al menos una institución con su precio.")
    private List<InstitutionPriceDto> institutions;
}
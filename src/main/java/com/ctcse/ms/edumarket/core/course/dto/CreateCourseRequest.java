package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
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

    @NotNull(message = "El costo del curso (courseCost) no puede ser nulo.")
    private BigDecimal courseCost;

    @NotNull(message = "El ID del tipo de curso (idCourseType) no puede ser nulo.")
    private Long idCourseType;

    @NotNull(message = "El ID de la modalidad (idModality) no puede ser nulo.")
    private Long idModality;

    @NotEmpty(message = "Debe proporcionar al menos un ID de institución.")
    private List<Long> idInstitutions;
}

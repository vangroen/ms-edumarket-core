package com.ctcse.ms.edumarket.core.institution.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstitutionPriceDto {

    @NotNull(message = "El objeto 'institution' no puede ser nulo.")
    private InstitutionDto institution;

    @NotNull(message = "El precio no puede ser nulo.")
    @Min(value = 0, message = "El precio no puede ser negativo.")
    private BigDecimal price;

    @NotNull(message = "La duración en meses no puede ser nula.")
    @Min(value = 1, message = "La duración debe ser de al menos 1 mes.")
    private Integer durationInMonths;
}

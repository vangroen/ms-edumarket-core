package com.ctcse.ms.edumarket.core.institution.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstitutionPriceDto {

    private InstitutionDto institution;
    private BigDecimal price;
}

package com.ctcse.ms.edumarket.core.profession.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfessionDto {

    private Long id;
    private String name;
}

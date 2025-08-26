package com.ctcse.ms.edumarket.core.profession.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProfessionRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe estar entre 3 y 100 caracteres")
    private String name;
}

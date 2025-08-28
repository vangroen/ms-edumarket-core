package com.ctcse.ms.edumarket.core.modality.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateModalityRequest {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 3, max = 100, message = "La descripción debe estar entre 3 y 100 caracteres")
    private String description;
}

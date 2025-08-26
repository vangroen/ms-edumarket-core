package com.ctcse.ms.edumarket.core.installmentStatus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateInstallmentStatusRequest {

    @NotBlank(message = "El status no puede estar vacío")
    @Size(min = 2, max = 30, message = "El status debe estar entre 2 y 30 caracteres")
    private String status;
}

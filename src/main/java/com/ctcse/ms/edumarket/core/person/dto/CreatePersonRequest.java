package com.ctcse.ms.edumarket.core.person.dto;

import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePersonRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe estar entre 3 y 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 3, max = 50, message = "El apellido debe estar entre 3 y 50 caracteres")
    private String lastName;

    @NotBlank(message = "El email no puede estar vacío")
    @Size(min = 3, max = 100, message = "El email debe estar entre 3 y 100 caracteres")
    private String email;

    @NotBlank(message = "El celular no puede estar vacío")
    @Size(min = 3, max = 20, message = "El celular debe estar entre 3 y 20 caracteres")
    private String phone;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 3, max = 255, message = "La dirección debe estar entre 3 y 255 caracteres")
    private String address;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Size(min = 3, max = 16, message = "El número de documento debe estar entre 3 y 16 caracteres")
    private String documentNumber;

    private Long idDocumentType;
}

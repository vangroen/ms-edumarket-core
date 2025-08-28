package com.ctcse.ms.edumarket.core.student.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStudentRequest {

    @NotNull(message = "El ID de la profesión no puede ser nulo.")
    private Long idProfession;

    @NotNull(message = "El ID de la institución no puede ser nulo.")
    private Long idInstitution;

    @NotNull(message = "El ID del rango académico no puede ser nulo.")
    private Long idAcademicRank;

    @NotNull(message = "El ID de la persona no puede ser nulo.")
    private Long idPerson;
}
package com.ctcse.ms.edumarket.core.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class UpdateEnrollmentRequest {

    @NotNull(message = "El costo total de la matrícula no puede ser nulo.")
    private BigDecimal totalEnrollmentCost;

    @NotNull(message = "La fecha de matrícula no puede ser nula.")
    private Instant enrollmentDate;

    @NotNull(message = "El ID del estudiante no puede ser nulo.")
    private Long idStudent;

    @NotNull(message = "El ID del agente no puede ser nulo.")
    private Long idAgent;

    @NotNull(message = "El ID del curso no puede ser nulo.")
    private Long idCourse;
}
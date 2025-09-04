package com.ctcse.ms.edumarket.core.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class UpdateEnrollmentRequest {

    @NotNull
    private Instant enrollmentDate;
    @NotNull
    private Long idStudent;
    @NotNull
    private Long idAgent;
    @NotNull
    private Long idCourse;
    @NotNull
    private Long idInstitution;
    @NotNull
    private BigDecimal enrollmentFeeAmount;
    @NotNull
    private BigDecimal finalRightsAmount;
}
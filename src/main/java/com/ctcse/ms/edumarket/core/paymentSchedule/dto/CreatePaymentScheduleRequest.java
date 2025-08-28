package com.ctcse.ms.edumarket.core.paymentSchedule.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreatePaymentScheduleRequest {

    private BigDecimal installmentAmount;

    private Instant installmentDueDate;

    private Long idConceptType;
    private Long idInstallmentStatus;
    private Long idEnrollment;
}

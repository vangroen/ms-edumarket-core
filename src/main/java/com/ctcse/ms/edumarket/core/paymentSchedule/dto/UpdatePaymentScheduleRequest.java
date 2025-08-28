package com.ctcse.ms.edumarket.core.paymentSchedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class UpdatePaymentScheduleRequest {

    @NotNull(message = "El monto de la cuota no puede ser nulo.")
    private BigDecimal installmentAmount;

    @NotNull(message = "La fecha de vencimiento no puede ser nula.")
    private Instant installmentDueDate;

    @NotNull(message = "El ID del tipo de concepto no puede ser nulo.")
    private Long idConceptType;

    @NotNull(message = "El ID del estado de la cuota no puede ser nulo.")
    private Long idInstallmentStatus;

    @NotNull(message = "El ID de la matrícula no puede ser nulo.")
    private Long idEnrollment;
}
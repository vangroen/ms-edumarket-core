package com.ctcse.ms.edumarket.core.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class UpdatePaymentRequest {

    @NotNull(message = "El monto del pago no puede ser nulo.")
    private BigDecimal paymentAmount;

    @NotNull(message = "La fecha de pago no puede ser nula.")
    private Instant paymentDate;

    @NotNull(message = "El ID del tipo de pago no puede ser nulo.")
    private Long idPaymentType;

    @NotNull(message = "El ID del cronograma de pago no puede ser nulo.")
    private Long idPaymentSchedule;
}
package com.ctcse.ms.edumarket.core.payment.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreatePaymentRequest {

    @NotBlankWithMessage(fieldName = "monto pagado")
    @Size(message = "El monto pagado debe ser mayor a cero")
    private BigDecimal paymentAmount;

    @NotBlankWithMessage(fieldName = "fecha de pago")
    @Size(message = "La fecha debe ser una actual")
    private Instant paymentDate;

    @NotBlankWithMessage(fieldName = "tipo de pago")
    @SizeWithMessage(fieldName = "tipo de pago", min = 1, max = 100)
    private Long idPaymentType;

    @NotBlankWithMessage(fieldName = "cronograma de pagos")
    @SizeWithMessage(fieldName = "cronograma de pagos", min = 1, max = 10000)
    private Long idPaymentSchedule;
}

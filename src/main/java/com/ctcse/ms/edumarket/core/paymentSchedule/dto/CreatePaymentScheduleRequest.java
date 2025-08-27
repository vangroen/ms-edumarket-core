package com.ctcse.ms.edumarket.core.paymentSchedule.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreatePaymentScheduleRequest {

    @NotBlankWithMessage(fieldName = "monto vencimiento de la cuota")
    private BigDecimal installmentAmount;

    @NotBlankWithMessage(fieldName = "fecha de vencimiento de la cuota")
    private Instant installmentDueDate;

    @NotBlankWithMessage(fieldName = "id del tipo de concepto")
    @SizeWithMessage(fieldName = "id del tipo de concepto", min = 1, max = 100)
    private Long idConceptType;

    @NotBlankWithMessage(fieldName = "id del estado de la cuota")
    @SizeWithMessage(fieldName = "id del estado de la cuota", min = 1, max = 100)
    private Long idInstallmentStatus;
}

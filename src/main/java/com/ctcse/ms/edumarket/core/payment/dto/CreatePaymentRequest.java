package com.ctcse.ms.edumarket.core.payment.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreatePaymentRequest {

    private BigDecimal paymentAmount;

    private Instant paymentDate;

    private Long idPaymentType;
    private Long idPaymentSchedule;
}

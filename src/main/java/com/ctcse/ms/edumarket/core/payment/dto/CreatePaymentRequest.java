package com.ctcse.ms.edumarket.core.payment.dto;

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

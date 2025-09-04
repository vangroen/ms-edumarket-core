package com.ctcse.ms.edumarket.core.payment.dto;

import com.ctcse.ms.edumarket.core.paymentType.dto.PaymentTypeDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentDetailDto {

    private Long paymentId;
    private BigDecimal paymentAmount;
    private Instant paymentDate;
    private PaymentTypeDto paymentType;
}

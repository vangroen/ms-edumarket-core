package com.ctcse.ms.edumarket.core.payment.dto;

import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.PaymentScheduleDto;
import com.ctcse.ms.edumarket.core.paymentType.dto.PaymentTypeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {

    private Long id;
    private BigDecimal paymentAmount;
    private PaymentScheduleDto paymentSchedule;
    private Boolean active;

    private Long enrollmentId;
    private Instant paymentDate;
    private PaymentTypeDto paymentType;
    private InstallmentStatusDto installmentStatus;
    private ConceptTypeDto conceptType;
    private String message;
}
package com.ctcse.ms.edumarket.core.paymentSchedule.dto;

import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.payment.dto.PaymentDetailDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class ScheduledPaymentDto {

    private Long scheduleId;
    private ConceptTypeDto conceptType;
    private BigDecimal installmentAmount;
    private Instant installmentDueDate;
    private InstallmentStatusDto installmentStatus;
    private List<PaymentDetailDto> paymentsMade;
}

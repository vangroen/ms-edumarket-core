package com.ctcse.ms.edumarket.core.paymentSchedule.dto;

import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.enrollment.dto.EnrollmentDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentScheduleDto {

    private Long id;
    private Long enrollmentId;
    private BigDecimal installmentAmount;
    private Instant installmentDueDate;
    private ConceptTypeDto conceptType;
    private InstallmentStatusDto installmentStatus;
    private boolean active;
}

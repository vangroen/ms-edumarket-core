package com.ctcse.ms.edumarket.core.enrollment.dto;

import com.ctcse.ms.edumarket.core.paymentSchedule.dto.ScheduledPaymentDto;
import lombok.Data;

import java.util.List;

@Data
public class FullEnrollmentDetailsDto {

    private EnrollmentDto enrollmentInfo;
    private List<ScheduledPaymentDto> paymentSchedule;
}

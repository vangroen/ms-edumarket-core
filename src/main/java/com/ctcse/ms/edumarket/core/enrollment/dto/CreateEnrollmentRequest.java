package com.ctcse.ms.edumarket.core.enrollment.dto;

import com.ctcse.ms.edumarket.core.agent.dto.AgentDto;
import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.student.dto.StudentDto;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreateEnrollmentRequest {

    @NotBlankWithMessage(fieldName = "costo de matrícula")
    @Size(message = "El costo de matrícula debe ser mayor a cero")
    private BigDecimal totalEnrollmentCost;

    @NotBlankWithMessage(fieldName = "fecha de matrícula")
    @Size(message = "La fecha de matrícula debe ser una actual")
    private Instant enrollmentDate;

    private Long idStudent;
    private Long idAgent;
    private Long idCourse;
}

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

//    private BigDecimal totalEnrollmentCost;

    private Instant enrollmentDate;
    private Long idStudent;
    private Long idAgent;
    private Long idCourse;
    private Long idInstitution; // <--- CAMBIO CLAVE: Añadir este campo

    // Estos montos los define el negocio y se envían desde el frontend
    private BigDecimal enrollmentFeeAmount;
    private BigDecimal finalRightsAmount;
}

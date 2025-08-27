package com.ctcse.ms.edumarket.core.enrollment.dto;

import com.ctcse.ms.edumarket.core.agent.dto.AgentDto;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.student.dto.StudentDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class EnrollmentDto {

    private Long id;
    private BigDecimal totalEnrollmentCost;
    private Instant enrollmentDate;
    private StudentDto student;
    private AgentDto agent;
    private CourseDto course;
}

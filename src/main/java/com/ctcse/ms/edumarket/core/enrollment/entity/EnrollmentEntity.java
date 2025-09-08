package com.ctcse.ms.edumarket.core.enrollment.entity;

import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEnrollment")
    private Long id;

    @Column(name = "totalEnrollmentCost", nullable = false)
    private BigDecimal totalEnrollmentCost;

    @Column(name = "enrollmentDate", nullable = false)
    private Instant enrollmentDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idStudent", nullable = false)
    private StudentEntity student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idAgent", nullable = false)
    private AgentEntity agent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCourse", nullable = false)
    private CourseEntity course;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idInstitution", nullable = false)
    private InstitutionEntity institution;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}

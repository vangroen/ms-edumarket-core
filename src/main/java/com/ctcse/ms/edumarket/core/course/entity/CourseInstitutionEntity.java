package com.ctcse.ms.edumarket.core.course.entity;

import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "course_institution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstitutionEntity {

    @EmbeddedId
    private CourseInstitutionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("institutionId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private InstitutionEntity institution;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "durationInMonths", nullable = false)
    private Integer durationInMonths;
}
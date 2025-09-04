package com.ctcse.ms.edumarket.core.course.entity;

import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "durationInMonths", nullable = false)
    private Integer durationInMonths;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCourseType", nullable = false)
    private CourseTypeEntity courseType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idModality", nullable = false)
    private ModalityEntity modality;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CourseInstitutionEntity> institutions;
}
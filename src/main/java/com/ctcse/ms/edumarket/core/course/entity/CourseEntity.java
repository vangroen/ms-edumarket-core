package com.ctcse.ms.edumarket.core.course.entity;

import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "courseCost", nullable = false)
    private BigDecimal courseCost;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCourseType", nullable = false)
    private CourseTypeEntity courseType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idModality", nullable = false)
    private ModalityEntity modality;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "course_institution",
            joinColumns = { @JoinColumn(name = "idCourse") },
            inverseJoinColumns = { @JoinColumn(name = "idInstitution") })
    private Set<InstitutionEntity> institutions = new HashSet<>();
}

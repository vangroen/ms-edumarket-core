package com.ctcse.ms.edumarket.core.institution.entity;

import com.ctcse.ms.edumarket.core.course.entity.CourseInstitutionEntity;
import com.ctcse.ms.edumarket.core.institutionType.entity.InstitutionTypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "institution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInstitution")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idInstitutionType", nullable = false)
    private InstitutionTypeEntity institutionType;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CourseInstitutionEntity> courses;
}

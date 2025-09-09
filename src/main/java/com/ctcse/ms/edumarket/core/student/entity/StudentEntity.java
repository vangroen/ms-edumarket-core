package com.ctcse.ms.edumarket.core.student.entity;

import com.ctcse.ms.edumarket.core.academicRank.entity.AcademicRankEntity;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import com.ctcse.ms.edumarket.core.profession.entity.ProfessionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStudent")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idProfession", nullable = false)
    private ProfessionEntity profession;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idInstitution", nullable = false)
    private InstitutionEntity institution;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idAcademicRank", nullable = false)
    private AcademicRankEntity academicRank;

    @OneToOne(optional = false)
    @JoinColumn(name = "idPerson", nullable = false)
    private PersonEntity person;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}

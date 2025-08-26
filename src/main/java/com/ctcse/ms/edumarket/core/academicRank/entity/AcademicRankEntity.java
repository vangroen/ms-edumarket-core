package com.ctcse.ms.edumarket.core.academicRank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "academic_rank")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicRankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAcademicRank")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}

package com.ctcse.ms.edumarket.core.institutionType.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "institution_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionTypeEntity {

    @Id
    @GeneratedValue
    @Column(name = "idInstitutionType")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}

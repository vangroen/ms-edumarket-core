package com.ctcse.ms.edumarket.core.conceptType.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concept_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConceptType")
    public Long id;

    @Column(name = "description", nullable = false, length = 100)
    public String description;
}

package com.ctcse.ms.edumarket.core.modality.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modality")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModalityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idModality")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}

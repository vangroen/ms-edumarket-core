package com.ctcse.ms.edumarket.core.profession.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profession")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProfession")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}

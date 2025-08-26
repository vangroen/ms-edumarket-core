package com.ctcse.ms.edumarket.core.installmentStatus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "installment_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idIstallmentStatus")
    public Long id;

    @Column(name = "status", nullable = false, length = 30)
    public String status;
}

package com.ctcse.ms.edumarket.core.paymentType.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPaymentType")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}

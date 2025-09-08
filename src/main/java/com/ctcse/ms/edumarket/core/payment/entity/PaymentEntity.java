package com.ctcse.ms.edumarket.core.payment.entity;

import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentType.entity.PaymentTypeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPayment")
    private Long id;

    @Column(name = "paymentAmount", nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "paymentDate", nullable = false)
    private Instant paymentDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idPaymentType", nullable = false)
    private PaymentTypeEntity paymentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idPaymentSchedule", nullable = false)
    private PaymentScheduleEntity paymentSchedule;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}

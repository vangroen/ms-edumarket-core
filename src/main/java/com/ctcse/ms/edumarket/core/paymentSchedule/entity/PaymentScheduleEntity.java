package com.ctcse.ms.edumarket.core.paymentSchedule.entity;

import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPaymentSchedule")
    private Long id;

    @Column(name = "installmentAmount", nullable = false)
    private BigDecimal installmentAmount;

    @Column(name = "installmentDueDate", nullable = false)
    private Instant installmentDueDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idConceptType", nullable = false)
    private ConceptTypeEntity conceptType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idInstallmentStatus", nullable = false)
    private InstallmentStatusEntity installmentStatus;

    @ManyToOne
    @JoinColumn(name = "idEnrollment", nullable = false) // Esto creará la columna 'enrollment_id' en la BD
    private EnrollmentEntity enrollment;
}

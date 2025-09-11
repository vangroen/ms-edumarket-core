package com.ctcse.ms.edumarket.core.paymentSchedule.repository;

import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentScheduleEntity, Long> {
    List<PaymentScheduleEntity> findByEnrollmentId(Long enrollmentId);
    Optional<PaymentScheduleEntity> findByEnrollmentIdAndConceptTypeId(Long enrollmentId, Long conceptTypeId);

    @Query("SELECT ps FROM PaymentScheduleEntity ps WHERE ps.installmentDueDate < :now AND ps.installmentStatus.id = 1")
    List<PaymentScheduleEntity> findOverdueInstallments(@Param("now") Instant now);

    List<PaymentScheduleEntity> findAllByActiveTrue();

    List<PaymentScheduleEntity> findAllByEnrollmentIdAndActiveFalse(Long enrollmentId);
}

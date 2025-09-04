package com.ctcse.ms.edumarket.core.paymentSchedule.repository;

import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentScheduleEntity, Long> {
    List<PaymentScheduleEntity> findByEnrollmentId(Long enrollmentId);
    Optional<PaymentScheduleEntity> findByEnrollmentIdAndConceptTypeId(Long enrollmentId, Long conceptTypeId);
}

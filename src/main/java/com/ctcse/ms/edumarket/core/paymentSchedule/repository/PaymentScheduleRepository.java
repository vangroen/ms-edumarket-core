package com.ctcse.ms.edumarket.core.paymentSchedule.repository;

import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentScheduleEntity, Long> {
}

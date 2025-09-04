package com.ctcse.ms.edumarket.core.payment.repository;

import com.ctcse.ms.edumarket.core.payment.entity.PaymentEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByPaymentScheduleId(Long paymentScheduleId);
    void deleteAllByPaymentScheduleIn(List<PaymentScheduleEntity> schedules);
}

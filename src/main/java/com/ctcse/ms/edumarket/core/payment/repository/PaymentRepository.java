package com.ctcse.ms.edumarket.core.payment.repository;

import com.ctcse.ms.edumarket.core.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}

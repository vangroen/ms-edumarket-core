package com.ctcse.ms.edumarket.core.paymentType.repository;

import com.ctcse.ms.edumarket.core.paymentType.entity.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Long> {
}

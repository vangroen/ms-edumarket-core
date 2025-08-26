package com.ctcse.ms.edumarket.core.installmentStatus.repository;

import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentStatusRepository extends JpaRepository<InstallmentStatusEntity, Long> {

}

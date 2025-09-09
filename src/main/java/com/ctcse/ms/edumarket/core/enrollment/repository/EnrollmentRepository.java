package com.ctcse.ms.edumarket.core.enrollment.repository;

import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {
    List<EnrollmentEntity> findAllByActiveTrue();
    List<EnrollmentEntity> findAllByStudentIdAndActiveTrue(Long studentId);
}

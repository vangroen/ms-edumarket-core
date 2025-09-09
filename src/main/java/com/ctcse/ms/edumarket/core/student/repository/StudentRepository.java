package com.ctcse.ms.edumarket.core.student.repository;

import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByPersonId(Long personId);
    Optional<StudentEntity> findByPersonIdAndActiveTrue(Long personId);
    List<StudentEntity> findAllByActiveTrue();
}

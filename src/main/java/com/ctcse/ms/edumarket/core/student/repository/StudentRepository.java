package com.ctcse.ms.edumarket.core.student.repository;

import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}

package com.ctcse.ms.edumarket.core.course.repository;

import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}

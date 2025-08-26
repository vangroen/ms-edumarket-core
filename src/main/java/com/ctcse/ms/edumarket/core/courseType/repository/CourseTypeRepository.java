package com.ctcse.ms.edumarket.core.courseType.repository;

import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTypeRepository extends JpaRepository<CourseTypeEntity, Long> {
}

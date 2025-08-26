package com.ctcse.ms.edumarket.core.academicRank.repository;

import com.ctcse.ms.edumarket.core.academicRank.entity.AcademicRankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicRankRepository extends JpaRepository<AcademicRankEntity, Long> {
}

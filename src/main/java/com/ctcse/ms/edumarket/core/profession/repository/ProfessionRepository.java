package com.ctcse.ms.edumarket.core.profession.repository;

import com.ctcse.ms.edumarket.core.profession.entity.ProfessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<ProfessionEntity, Long> {
}

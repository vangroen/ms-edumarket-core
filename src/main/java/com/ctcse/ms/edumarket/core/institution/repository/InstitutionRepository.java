package com.ctcse.ms.edumarket.core.institution.repository;

import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long> {
}

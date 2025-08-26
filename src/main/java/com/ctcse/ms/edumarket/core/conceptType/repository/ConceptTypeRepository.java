package com.ctcse.ms.edumarket.core.conceptType.repository;

import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptTypeRepository extends JpaRepository<ConceptTypeEntity, Long> {
}

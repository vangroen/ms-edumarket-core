package com.ctcse.ms.edumarket.core.documentType.repository;

import com.ctcse.ms.edumarket.core.documentType.entity.DocumentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {

}

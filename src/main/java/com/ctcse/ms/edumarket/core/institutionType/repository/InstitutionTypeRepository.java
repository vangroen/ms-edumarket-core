package com.ctcse.ms.edumarket.core.institutionType.repository;

import com.ctcse.ms.edumarket.core.institutionType.entity.InstitutionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionTypeRepository extends JpaRepository<InstitutionTypeEntity, Long> {

}

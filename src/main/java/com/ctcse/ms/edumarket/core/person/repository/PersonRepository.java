package com.ctcse.ms.edumarket.core.person.repository;

import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByDocumentNumber(String documentNumber);
}

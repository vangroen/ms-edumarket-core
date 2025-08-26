package com.ctcse.ms.edumarket.core.modality.repository;

import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModalityRepository extends JpaRepository<ModalityEntity, Long> {
}

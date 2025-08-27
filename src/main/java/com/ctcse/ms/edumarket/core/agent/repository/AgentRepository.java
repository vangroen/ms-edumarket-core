package com.ctcse.ms.edumarket.core.agent.repository;

import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Long> {
}

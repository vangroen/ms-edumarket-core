package com.ctcse.ms.edumarket.core.agent.entity;

import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAgent")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "idPerson", nullable = false)
    private PersonEntity person;
}

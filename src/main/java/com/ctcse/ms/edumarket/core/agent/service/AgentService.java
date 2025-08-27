package com.ctcse.ms.edumarket.core.agent.service;

import com.ctcse.ms.edumarket.core.agent.dto.AgentDto;
import com.ctcse.ms.edumarket.core.agent.dto.CreateAgentRequest;
import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import com.ctcse.ms.edumarket.core.agent.repository.AgentRepository;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.documentType.entity.DocumentTypeEntity;
import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import com.ctcse.ms.edumarket.core.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<AgentDto> findAll() {
        List<AgentEntity> entities = agentRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AgentDto convertToDto(AgentEntity entity) {
        AgentDto dto = new AgentDto();
        dto.setId(entity.getId());

        if (entity.getPerson() != null) {
            final var personDto = getPersonDto(entity.getPerson());
            dto.setPerson(personDto);
        }

        return dto;
    }

    private PersonDto getPersonDto(PersonEntity person) {
        var personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setFirstName(person.getFirstName());
        personDto.setLastName(person.getLastName());
        personDto.setEmail(person.getEmail());
        personDto.setPhone(person.getPhone());
        personDto.setAddress(person.getAddress());
        personDto.setDocumentNumber(person.getDocumentNumber());

        if (person.getDocumentType() != null) {
            var documentType = person.getDocumentType();
            var documentTypeDto = new DocumentTypeDto();
            documentTypeDto.setId(documentType.getId());
            documentTypeDto.setDescription(documentType.getDescription());

            personDto.setDocumentType(documentTypeDto);
        }

        return personDto;
    }

    @Transactional
    public AgentDto create(CreateAgentRequest request) {
        PersonEntity personEntity = personRepository.findById(request.getIdPerson())
                .orElseThrow(() -> new ResourceNotFoundException("La persona con id " + request.getIdPerson() + " no fue encontrado"));

        AgentEntity agentEntity = new AgentEntity();
        agentEntity.setPerson(personEntity);

        AgentEntity savedAgentEntity = agentRepository.save(agentEntity);

        return convertToDto(savedAgentEntity);
    }
}

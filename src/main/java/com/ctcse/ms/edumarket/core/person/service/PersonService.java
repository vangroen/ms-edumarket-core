package com.ctcse.ms.edumarket.core.person.service;

import com.ctcse.ms.edumarket.core.common.exception.ResourceAlreadyExistsException;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.documentType.entity.DocumentTypeEntity;
import com.ctcse.ms.edumarket.core.documentType.repository.DocumentTypeRepository;
import com.ctcse.ms.edumarket.core.person.dto.CreatePersonRequest;
import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import com.ctcse.ms.edumarket.core.person.dto.UpdatePersonRequest;
import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import com.ctcse.ms.edumarket.core.person.repository.PersonRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final DocumentTypeRepository documentTypeRepository;

    @Transactional(readOnly = true)
    public List<PersonDto> findAll() {
        List<PersonEntity> entities = personRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PersonDto convertToDto(PersonEntity entity) {
        PersonDto dto = new PersonDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setDocumentNumber(entity.getDocumentNumber());

        if (entity.getDocumentType() != null) {
            var docTypeDto = new DocumentTypeDto();
            docTypeDto.setId(entity.getDocumentType().getId());
            docTypeDto.setDescription(entity.getDocumentType().getDescription());
            dto.setDocumentType(docTypeDto);
        }

        return dto;
    }

    @Transactional
    public PersonDto create(CreatePersonRequest request) {
        personRepository.findByDocumentNumber(request.getDocumentNumber()).ifPresent(p -> {
            throw new ResourceAlreadyExistsException("Ya existe una persona con el número de documento " + request.getDocumentNumber());
        });

        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findById(request.getIdDocumentType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de documento con id " + request.getIdDocumentType() + " no fue encontrado."));

        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(request.getFirstName());
        personEntity.setLastName(request.getLastName());
        personEntity.setEmail(request.getEmail());
        personEntity.setPhone(request.getPhone());
        personEntity.setAddress(request.getAddress());
        personEntity.setDocumentType(documentTypeEntity);
        personEntity.setDocumentNumber(request.getDocumentNumber());

        PersonEntity savedPersonEntity = personRepository.save(personEntity);
        return convertToDto(savedPersonEntity);
    }

    @Transactional
    public PersonDto update(Long id, UpdatePersonRequest request) {
        PersonEntity personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La persona con id " + id + " no fue encontrada."));

        Optional<PersonEntity> existingPersonByDoc = personRepository.findByDocumentNumber(request.getDocumentNumber());
        if (existingPersonByDoc.isPresent() && !existingPersonByDoc.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("El número de documento " + request.getDocumentNumber() + " ya está en uso por otra persona.");
        }

        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findById(request.getIdDocumentType())
                .orElseThrow(() -> new ResourceNotFoundException("El tipo de documento con id " + request.getIdDocumentType() + " no fue encontrado."));

        personEntity.setFirstName(request.getFirstName());
        personEntity.setLastName(request.getLastName());
        personEntity.setEmail(request.getEmail());
        personEntity.setPhone(request.getPhone());
        personEntity.setAddress(request.getAddress());
        personEntity.setDocumentNumber(request.getDocumentNumber());
        personEntity.setDocumentType(documentTypeEntity);

        PersonEntity updatedEntity = personRepository.save(personEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public PersonDto findById(Long id) {
        PersonEntity personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La persona con id " + id + " no fue encontrada."));
        return convertToDto(personEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("La persona con id " + id + " no fue encontrada.");
        }
        personRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PersonDto findByDocumentNumber(String documentNumber) {
        PersonEntity personEntity = personRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("La persona con el documento " + documentNumber + " no fue encontrada."));
        return convertToDto(personEntity);
    }
}

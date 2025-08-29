package com.ctcse.ms.edumarket.core.student.service;

import com.ctcse.ms.edumarket.core.academicRank.dto.AcademicRankDto;
import com.ctcse.ms.edumarket.core.academicRank.entity.AcademicRankEntity;
import com.ctcse.ms.edumarket.core.academicRank.repository.AcademicRankRepository;
import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.institution.repository.InstitutionRepository;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import com.ctcse.ms.edumarket.core.person.repository.PersonRepository;
import com.ctcse.ms.edumarket.core.profession.dto.ProfessionDto;
import com.ctcse.ms.edumarket.core.profession.entity.ProfessionEntity;
import com.ctcse.ms.edumarket.core.profession.repository.ProfessionRepository;
import com.ctcse.ms.edumarket.core.student.dto.CreateStudentRequest;
import com.ctcse.ms.edumarket.core.student.dto.StudentDto;
import com.ctcse.ms.edumarket.core.student.dto.UpdateStudentRequest;
import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import com.ctcse.ms.edumarket.core.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ProfessionRepository professionRepository;
    private final InstitutionRepository institutionRepository;
    private final AcademicRankRepository academicRankRepository;
    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        List<StudentEntity> entities = studentRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StudentDto convertToDto(StudentEntity entity) {
        StudentDto dto = new StudentDto();
        dto.setId(entity.getId());

        if (entity.getProfession() != null) {
            var professionDto = new ProfessionDto();
            professionDto.setId(entity.getProfession().getId());
            professionDto.setName(entity.getProfession().getName());
            dto.setProfession(professionDto);
        }

        if (entity.getInstitution() != null) {
            final var institutionDto = getInstitutionDto(entity.getInstitution());
            dto.setInstitution(institutionDto);
        }

        if (entity.getAcademicRank() != null) {
            var academicRankDto = new AcademicRankDto();
            academicRankDto.setId(entity.getAcademicRank().getId());
            academicRankDto.setDescription(entity.getAcademicRank().getDescription());
            dto.setAcademicRank(academicRankDto);
        }

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

    private InstitutionDto getInstitutionDto(InstitutionEntity institution) {
        var institutionDto = new InstitutionDto();
        institutionDto.setId(institution.getId());
        institutionDto.setName(institution.getName());

        if (institution.getInstitutionType() != null) {
            var institutionType = institution.getInstitutionType();
            var institutionTypeDto = new InstitutionTypeDto();
            institutionTypeDto.setId(institutionType.getId());
            institutionTypeDto.setDescription(institutionType.getDescription());

            institutionDto.setInstitutionType(institutionTypeDto);
        }

        return institutionDto;
    }

    @Transactional
    public StudentDto create(CreateStudentRequest request) {
        ProfessionEntity professionEntity = professionRepository.findById(request.getIdProfession())
                .orElseThrow(() -> new ResourceNotFoundException("La profesion con id " + request.getIdProfession() + " no fue encontrado"));

        InstitutionEntity institutionEntity = institutionRepository.findById(request.getIdInstitution())
                .orElseThrow(() ->new ResourceNotFoundException("La institucion con id " + request.getIdInstitution() + " no fue encontrado"));

        AcademicRankEntity academicRankEntity = academicRankRepository.findById(request.getIdAcademicRank())
                .orElseThrow(() -> new ResourceNotFoundException("El rango académico con id " + request.getIdAcademicRank() + " no fue encontrado"));

        PersonEntity personEntity = personRepository.findById(request.getIdPerson())
                .orElseThrow(() -> new ResourceNotFoundException("La persona con id " + request.getIdPerson() + " no fue encontrado"));

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setProfession(professionEntity);
        studentEntity.setInstitution(institutionEntity);
        studentEntity.setAcademicRank(academicRankEntity);
        studentEntity.setPerson(personEntity);

        StudentEntity savedStudentEntity = studentRepository.save(studentEntity);

        return convertToDto(savedStudentEntity);
    }

    @Transactional
    public StudentDto update(Long id, UpdateStudentRequest request) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El estudiante con id " + id + " no fue encontrado."));

        ProfessionEntity professionEntity = professionRepository.findById(request.getIdProfession())
                .orElseThrow(() -> new ResourceNotFoundException("La profesión con id " + request.getIdProfession() + " no fue encontrada"));

        InstitutionEntity institutionEntity = institutionRepository.findById(request.getIdInstitution())
                .orElseThrow(() -> new ResourceNotFoundException("La institución con id " + request.getIdInstitution() + " no fue encontrada"));

        AcademicRankEntity academicRankEntity = academicRankRepository.findById(request.getIdAcademicRank())
                .orElseThrow(() -> new ResourceNotFoundException("El rango académico con id " + request.getIdAcademicRank() + " no fue encontrado"));

        PersonEntity personEntity = personRepository.findById(request.getIdPerson())
                .orElseThrow(() -> new ResourceNotFoundException("La persona con id " + request.getIdPerson() + " no fue encontrada"));

        studentEntity.setProfession(professionEntity);
        studentEntity.setInstitution(institutionEntity);
        studentEntity.setAcademicRank(academicRankEntity);
        studentEntity.setPerson(personEntity);

        StudentEntity updatedEntity = studentRepository.save(studentEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public StudentDto findById(Long id) {
        StudentEntity entity = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El estudiante con id " + id + " no fue encontrado."));
        return convertToDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("El estudiante con id " + id + " no fue encontrado.");
        }
        studentRepository.deleteById(id);
    }
}

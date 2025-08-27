package com.ctcse.ms.edumarket.core.enrollment.service;

import com.ctcse.ms.edumarket.core.agent.dto.AgentDto;
import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import com.ctcse.ms.edumarket.core.agent.repository.AgentRepository;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.course.repository.CourseRepository;
import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.enrollment.dto.CreateEnrollmentRequest;
import com.ctcse.ms.edumarket.core.enrollment.dto.EnrollmentDto;
import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import com.ctcse.ms.edumarket.core.enrollment.repository.EnrollmentRepository;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.modality.entity.ModalityEntity;
import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import com.ctcse.ms.edumarket.core.person.entity.PersonEntity;
import com.ctcse.ms.edumarket.core.student.dto.StudentDto;
import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import com.ctcse.ms.edumarket.core.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final AgentRepository agentRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<EnrollmentDto> findAll() {
        List<EnrollmentEntity> entities = enrollmentRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EnrollmentDto convertToDto(EnrollmentEntity entity) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(entity.getId());
        dto.setTotalEnrollmentCost(entity.getTotalEnrollmentCost());
        dto.setEnrollmentDate(entity.getEnrollmentDate());

        if (entity.getCourse() != null) {
            final var courseDto = getCourseDto(entity.getCourse());
            dto.setCourse(courseDto);
        }

        if (entity.getAgent() != null) {
            final var agentDto = getAgentDto(entity.getAgent());
            dto.setAgent(agentDto);
        }

        if (entity.getStudent() != null) {
            final var studentDto = getStudentDto(entity.getStudent());
            dto.setStudent(studentDto);
        }

        return dto;
    }

    private CourseDto getCourseDto(CourseEntity course) {
        var courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setCourseCost(course.getCourseCost());

        if (course.getInstitution() != null) {
            var institution = course.getInstitution();
            final var institutionDto = getInstitutionDto(institution);

            courseDto.setInstitution(institutionDto);
        }

        if (course.getModality() != null) {
            var modality = course.getModality();
            final var modalityDto = getModalityDto(modality);

            courseDto.setModality(modalityDto);
        }

        if (course.getCourseType() != null) {
            var courseType = course.getCourseType();
            final var courseTypeDto = getCourseTypeDto(courseType);

            courseDto.setCourseType(courseTypeDto);
        }

        return courseDto;
    }

    private static InstitutionDto getInstitutionDto(InstitutionEntity institution) {
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

    private static ModalityDto getModalityDto(ModalityEntity modality) {
        var modalityDto = new ModalityDto();
        modalityDto.setId(modality.getId());
        modalityDto.setDescription(modality.getDescription());

        return modalityDto;
    }

    private static CourseTypeDto getCourseTypeDto(CourseTypeEntity courseType) {
        var courseTypeDto = new CourseTypeDto();
        courseTypeDto.setId(courseType.getId());
        courseTypeDto.setDescription(courseType.getDescription());

        return courseTypeDto;
    }

    private AgentDto getAgentDto(AgentEntity agent) {
        var agentDto = new AgentDto();
        agentDto.setId(agent.getId());

        if (agent.getPerson() != null) {
            var person = agent.getPerson();
            final var personDto = getPersonDto(person);

            agentDto.setPerson(personDto);
        }

        return agentDto;
    }

    private static PersonDto getPersonDto(PersonEntity person) {
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

    private StudentDto getStudentDto(StudentEntity student) {
        var studentDto = new StudentDto();
        studentDto.setId(student.getId());

        if (student.getPerson() != null) {
            var person = student.getPerson();
            final var personDto = getPersonDto(person);

            studentDto.setPerson(personDto);
        }

        return studentDto;
    }

    public EnrollmentDto create(CreateEnrollmentRequest request) {
        StudentEntity studentEntity = studentRepository.findById(request.getIdStudent())
                .orElseThrow(() -> new ResourceNotFoundException("El estudiante con id " + request.getIdStudent() + " no fue encontrado."));

        AgentEntity agentEntity = agentRepository.findById(request.getIdAgent())
                .orElseThrow(() -> new ResourceNotFoundException("El promotor con id " + request.getIdAgent() + " no fue encontrado."));

        CourseEntity courseEntity = courseRepository.findById(request.getIdCourse())
                .orElseThrow(() -> new ResourceNotFoundException("El curso con id " + request.getIdCourse() + " no fue encontrado."));

        EnrollmentEntity enrollmentEntity = new EnrollmentEntity();
        enrollmentEntity.setTotalEnrollmentCost(request.getTotalEnrollmentCost());
        enrollmentEntity.setEnrollmentDate(request.getEnrollmentDate());
        enrollmentEntity.setStudent(studentEntity);
        enrollmentEntity.setAgent(agentEntity);
        enrollmentEntity.setCourse(courseEntity);

        EnrollmentEntity savedEnrollmentEntity = enrollmentRepository.save(enrollmentEntity);
        return convertToDto(savedEnrollmentEntity);
    }
}

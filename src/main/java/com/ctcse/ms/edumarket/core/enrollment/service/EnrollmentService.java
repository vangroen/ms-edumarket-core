package com.ctcse.ms.edumarket.core.enrollment.service;

import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import com.ctcse.ms.edumarket.core.agent.repository.AgentRepository;
import com.ctcse.ms.edumarket.core.agent.service.AgentService;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.course.repository.CourseRepository;
import com.ctcse.ms.edumarket.core.course.service.CourseService;
import com.ctcse.ms.edumarket.core.enrollment.dto.CreateEnrollmentRequest;
import com.ctcse.ms.edumarket.core.enrollment.dto.EnrollmentDto;
import com.ctcse.ms.edumarket.core.enrollment.dto.UpdateEnrollmentRequest;
import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import com.ctcse.ms.edumarket.core.enrollment.repository.EnrollmentRepository;
import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import com.ctcse.ms.edumarket.core.student.repository.StudentRepository;
import com.ctcse.ms.edumarket.core.student.service.StudentService;
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

    private final StudentService studentService;
    private final AgentService agentService;
    private final CourseService courseService;

    @Transactional(readOnly = true)
    public List<EnrollmentDto> findAll() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDto create(CreateEnrollmentRequest request) {
        StudentEntity studentEntity = studentRepository.findById(request.getIdStudent())
                .orElseThrow(() -> new ResourceNotFoundException("El estudiante con id " + request.getIdStudent() + " no fue encontrado"));

        AgentEntity agentEntity = agentRepository.findById(request.getIdAgent())
                .orElseThrow(() -> new ResourceNotFoundException("El agente con id " + request.getIdAgent() + " no fue encontrado"));

        CourseEntity courseEntity = courseRepository.findById(request.getIdCourse())
                .orElseThrow(() -> new ResourceNotFoundException("El curso con id " + request.getIdCourse() + " no fue encontrado"));

        EnrollmentEntity enrollmentEntity = new EnrollmentEntity();
        enrollmentEntity.setTotalEnrollmentCost(request.getTotalEnrollmentCost());
        enrollmentEntity.setEnrollmentDate(request.getEnrollmentDate());
        enrollmentEntity.setStudent(studentEntity);
        enrollmentEntity.setAgent(agentEntity);
        enrollmentEntity.setCourse(courseEntity);

        EnrollmentEntity savedEntity = enrollmentRepository.save(enrollmentEntity);

        return convertToDto(savedEntity);
    }

    public EnrollmentDto convertToDto(EnrollmentEntity entity) {
        if (entity == null) {
            return null;
        }

        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(entity.getId());
        dto.setTotalEnrollmentCost(entity.getTotalEnrollmentCost());
        dto.setEnrollmentDate(entity.getEnrollmentDate());

        if (entity.getStudent() != null) {
            dto.setStudent(studentService.convertToDto(entity.getStudent()));
        }
        if (entity.getAgent() != null) {
            dto.setAgent(agentService.convertToDto(entity.getAgent()));
        }
        if (entity.getCourse() != null) {
            dto.setCourse(courseService.convertToDto(entity.getCourse()));
        }

        return dto;
    }

    @Transactional
    public EnrollmentDto update(Long id, UpdateEnrollmentRequest request) {
        EnrollmentEntity enrollmentEntity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La matrícula con id " + id + " no fue encontrada."));

        StudentEntity studentEntity = studentRepository.findById(request.getIdStudent())
                .orElseThrow(() -> new ResourceNotFoundException("El estudiante con id " + request.getIdStudent() + " no fue encontrado"));

        AgentEntity agentEntity = agentRepository.findById(request.getIdAgent())
                .orElseThrow(() -> new ResourceNotFoundException("El agente con id " + request.getIdAgent() + " no fue encontrado"));

        CourseEntity courseEntity = courseRepository.findById(request.getIdCourse())
                .orElseThrow(() -> new ResourceNotFoundException("El curso con id " + request.getIdCourse() + " no fue encontrado"));

        enrollmentEntity.setTotalEnrollmentCost(request.getTotalEnrollmentCost());
        enrollmentEntity.setEnrollmentDate(request.getEnrollmentDate());
        enrollmentEntity.setStudent(studentEntity);
        enrollmentEntity.setAgent(agentEntity);
        enrollmentEntity.setCourse(courseEntity);

        EnrollmentEntity updatedEntity = enrollmentRepository.save(enrollmentEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public EnrollmentDto findById(Long id) {
        EnrollmentEntity entity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La matrícula con id " + id + " no fue encontrada."));
        return convertToDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("La matrícula con id " + id + " no fue encontrada.");
        }
        enrollmentRepository.deleteById(id);
    }
}
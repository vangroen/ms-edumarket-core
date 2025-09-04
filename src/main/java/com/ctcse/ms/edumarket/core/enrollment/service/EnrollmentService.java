package com.ctcse.ms.edumarket.core.enrollment.service;

import com.ctcse.ms.edumarket.core.agent.entity.AgentEntity;
import com.ctcse.ms.edumarket.core.agent.repository.AgentRepository;
import com.ctcse.ms.edumarket.core.agent.service.AgentService;
import com.ctcse.ms.edumarket.core.common.exception.ResourceNotFoundException;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.repository.ConceptTypeRepository;
import com.ctcse.ms.edumarket.core.course.entity.CourseEntity;
import com.ctcse.ms.edumarket.core.course.entity.CourseInstitutionEntity;
import com.ctcse.ms.edumarket.core.course.repository.CourseRepository;
import com.ctcse.ms.edumarket.core.course.service.CourseService;
import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.enrollment.dto.*;
import com.ctcse.ms.edumarket.core.enrollment.entity.EnrollmentEntity;
import com.ctcse.ms.edumarket.core.enrollment.repository.EnrollmentRepository;
import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.entity.InstitutionEntity;
import com.ctcse.ms.edumarket.core.institution.repository.InstitutionRepository;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.payment.dto.PaymentDetailDto;
import com.ctcse.ms.edumarket.core.payment.entity.PaymentEntity;
import com.ctcse.ms.edumarket.core.payment.repository.PaymentRepository;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.ScheduledPaymentDto;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.repository.PaymentScheduleRepository;
import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import com.ctcse.ms.edumarket.core.student.repository.StudentRepository;
import com.ctcse.ms.edumarket.core.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final AgentRepository agentRepository;
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository; // Añadir
    private final PaymentScheduleRepository paymentScheduleRepository; // Añadir
    private final ConceptTypeRepository conceptTypeRepository; // Añadir
    private final InstallmentStatusRepository installmentStatusRepository;
    private final PaymentRepository paymentRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));
        AgentEntity agentEntity = agentRepository.findById(request.getIdAgent())
                .orElseThrow(() -> new ResourceNotFoundException("Agente no encontrado"));
        CourseEntity courseEntity = courseRepository.findById(request.getIdCourse())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));

        InstitutionEntity institutionEntity = institutionRepository.findById(request.getIdInstitution())
                .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada..."));

        BigDecimal coursePriceForInstitution = courseEntity.getInstitutions().stream()
                .filter(ci -> ci.getInstitution().getId().equals(request.getIdInstitution()))
                .map(CourseInstitutionEntity::getPrice)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("El precio para este curso en la institución seleccionada no fue encontrado."));

        BigDecimal totalCost = request.getEnrollmentFeeAmount()
                .add(coursePriceForInstitution)
                .add(request.getFinalRightsAmount());

        EnrollmentEntity enrollmentEntity = new EnrollmentEntity();
        enrollmentEntity.setTotalEnrollmentCost(totalCost);
        enrollmentEntity.setEnrollmentDate(request.getEnrollmentDate());
        enrollmentEntity.setStudent(studentEntity);
        enrollmentEntity.setAgent(agentEntity);
        enrollmentEntity.setCourse(courseEntity);
        enrollmentEntity.setInstitution(institutionEntity);

        EnrollmentEntity savedEntity = enrollmentRepository.save(enrollmentEntity);

        generatePaymentScheduleForEnrollment(savedEntity, request, coursePriceForInstitution, courseEntity.getDurationInMonths());

        return convertToDto(savedEntity);
    }

    private void generatePaymentScheduleForEnrollment(EnrollmentEntity enrollment, CreateEnrollmentRequest request, BigDecimal coursePrice, int durationInMonths) {
        ConceptTypeEntity enrollmentConcept = conceptTypeRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Tipo de Concepto 'Matrícula' no encontrado"));
        ConceptTypeEntity monthlyFeeConcept = conceptTypeRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("Tipo de Concepto 'Cuota Mensual' no encontrado"));
        ConceptTypeEntity finalRightsConcept = conceptTypeRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Tipo de Concepto 'Derechos Finales' no encontrado"));
        InstallmentStatusEntity pendingStatus = installmentStatusRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Estado de cuota 'Pendiente' no encontrado"));

        // --- CAMBIO CLAVE 1: Convertir el Instant inicial a ZonedDateTime ---
        // Usamos ZoneOffset.UTC para mantener la consistencia en el servidor.
        ZonedDateTime enrollmentZonedDateTime = request.getEnrollmentDate().atZone(ZoneOffset.UTC);

        // Crear la cuota de Matrícula (sin cambios aquí)
        PaymentScheduleEntity enrollmentFee = new PaymentScheduleEntity();
        enrollmentFee.setEnrollment(enrollment);
        enrollmentFee.setConceptType(enrollmentConcept);
        enrollmentFee.setInstallmentAmount(request.getEnrollmentFeeAmount());
        enrollmentFee.setInstallmentDueDate(request.getEnrollmentDate());
        enrollmentFee.setInstallmentStatus(pendingStatus);
        paymentScheduleRepository.save(enrollmentFee);

        // Crear las cuotas mensuales del curso
        BigDecimal monthlyInstallmentAmount = coursePrice.divide(new BigDecimal(durationInMonths), 2, RoundingMode.HALF_UP);
        for (int i = 1; i <= durationInMonths; i++) {
            PaymentScheduleEntity monthlyFee = new PaymentScheduleEntity();
            monthlyFee.setEnrollment(enrollment);
            monthlyFee.setConceptType(monthlyFeeConcept);
            monthlyFee.setInstallmentAmount(monthlyInstallmentAmount);

            // --- CAMBIO CLAVE 2: Usar ZonedDateTime para sumar meses y luego convertir a Instant ---
            ZonedDateTime nextDueDate = enrollmentZonedDateTime.plusMonths(i);
            monthlyFee.setInstallmentDueDate(nextDueDate.toInstant());

            monthlyFee.setInstallmentStatus(pendingStatus);
            paymentScheduleRepository.save(monthlyFee);
        }

        // Crear la cuota de Derechos Finales
        PaymentScheduleEntity finalRightsFee = new PaymentScheduleEntity();
        finalRightsFee.setEnrollment(enrollment);
        finalRightsFee.setConceptType(finalRightsConcept);
        finalRightsFee.setInstallmentAmount(request.getFinalRightsAmount());

        // --- CAMBIO CLAVE 3: Hacer lo mismo para la cuota final ---
        ZonedDateTime finalRightsDueDate = enrollmentZonedDateTime.plusMonths(durationInMonths + 1);
        finalRightsFee.setInstallmentDueDate(finalRightsDueDate.toInstant());

        finalRightsFee.setInstallmentStatus(pendingStatus);
        paymentScheduleRepository.save(finalRightsFee);
    }

    @Transactional(readOnly = true)
    public FullEnrollmentDetailsDto getFullEnrollmentDetails(Long enrollmentId) {
        // 1. Obtener la matrícula
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada"));

        // 2. Obtener el cronograma de pagos completo
        List<PaymentScheduleEntity> scheduleEntities = paymentScheduleRepository.findByEnrollmentId(enrollmentId);

        // 3. Mapear el cronograma a DTOs, incluyendo los pagos realizados
        List<ScheduledPaymentDto> scheduleDtos = scheduleEntities.stream().map(schedule -> {
            ScheduledPaymentDto scheduleDto = new ScheduledPaymentDto();
            // ... mapear campos de schedule a scheduleDto ...

            // Buscar los pagos para esta cuota del cronograma
            List<PaymentEntity> payments = paymentRepository.findByPaymentScheduleId(schedule.getId()); // Necesitarás crear este método
            List<PaymentDetailDto> paymentDtos = payments.stream().map(payment -> {
                PaymentDetailDto paymentDto = new PaymentDetailDto();
                // ... mapear campos de payment a paymentDto ...
                return paymentDto;
            }).collect(Collectors.toList());

            scheduleDto.setPaymentsMade(paymentDtos);
            return scheduleDto;
        }).collect(Collectors.toList());

        // 4. Ensamblar la respuesta final
        FullEnrollmentDetailsDto fullDetails = new FullEnrollmentDetailsDto();
        fullDetails.setEnrollmentInfo(convertToDto(enrollment));
        fullDetails.setPaymentSchedule(scheduleDtos);

        return fullDetails;
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
            final EnrolledCourseDto courseDto = getEnrolledCourseDto(entity);
            dto.setCourse(courseDto);
        }

        if (entity.getInstitution() != null) {
            final InstitutionDto institutionDto = getInstitutionDto(entity);
            dto.setInstitution(institutionDto);
        }

        paymentScheduleRepository.findByEnrollmentIdAndConceptTypeId(entity.getId(), 1L)
                .ifPresent(schedule -> dto.setEnrollmentFeeAmount(schedule.getInstallmentAmount()));

        // Buscamos la cuota de Derechos Finales (ConceptType ID = 3)
        paymentScheduleRepository.findByEnrollmentIdAndConceptTypeId(entity.getId(), 3L)
                .ifPresent(schedule -> dto.setFinalRightsAmount(schedule.getInstallmentAmount()));


        return dto;
    }

    private static InstitutionDto getInstitutionDto(EnrollmentEntity entity) {
        InstitutionDto institutionDto = new InstitutionDto();
        institutionDto.setId(entity.getInstitution().getId());
        institutionDto.setName(entity.getInstitution().getName());

        if (entity.getInstitution().getInstitutionType() != null) {
            InstitutionTypeDto institutionTypeDto = new InstitutionTypeDto();
            institutionTypeDto.setId(entity.getInstitution().getInstitutionType().getId());
            institutionTypeDto.setDescription(entity.getInstitution().getInstitutionType().getDescription());
            institutionDto.setInstitutionType(institutionTypeDto);
        }
        return institutionDto;
    }

    private static EnrolledCourseDto getEnrolledCourseDto(EnrollmentEntity entity) {
        EnrolledCourseDto courseDto = new EnrolledCourseDto();
        courseDto.setId(entity.getCourse().getId());
        courseDto.setName(entity.getCourse().getName());
        courseDto.setDurationInMonths(entity.getCourse().getDurationInMonths());

        if (entity.getCourse().getCourseType() != null) {
            CourseTypeDto courseTypeDto = new CourseTypeDto();
            courseTypeDto.setId(entity.getCourse().getCourseType().getId());
            courseTypeDto.setDescription(entity.getCourse().getCourseType().getDescription());
            courseDto.setCourseType(courseTypeDto);
        }

        if (entity.getCourse().getModality() != null) {
            ModalityDto modalityDto = new ModalityDto();
            modalityDto.setId(entity.getCourse().getModality().getId());
            modalityDto.setDescription(entity.getCourse().getModality().getDescription());
            courseDto.setModality(modalityDto);
        }
        return courseDto;
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
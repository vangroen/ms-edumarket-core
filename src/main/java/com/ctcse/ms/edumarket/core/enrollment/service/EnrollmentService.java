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
import com.ctcse.ms.edumarket.core.paymentSchedule.service.PaymentScheduleService;
import com.ctcse.ms.edumarket.core.student.entity.StudentEntity;
import com.ctcse.ms.edumarket.core.student.repository.StudentRepository;
import com.ctcse.ms.edumarket.core.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final AgentRepository agentRepository;
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final ConceptTypeRepository conceptTypeRepository;
    private final InstallmentStatusRepository installmentStatusRepository;
    private final PaymentRepository paymentRepository;

    @Lazy
    private final StudentService studentService;
    private final AgentService agentService;
    private final CourseService courseService;
    @Lazy
    private final PaymentScheduleService paymentScheduleService;

    public EnrollmentDto convertToDto(EnrollmentEntity entity) {
        if (entity == null) {
            return null;
        }
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(entity.getId());
        dto.setTotalEnrollmentCost(entity.getTotalEnrollmentCost());
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setActive(entity.isActive());
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

        // --- LÓGICA OPTIMIZADA ---
        // 1. Obtenemos todo el cronograma en una sola consulta.
        List<PaymentScheduleEntity> schedule = paymentScheduleRepository.findByEnrollmentId(entity.getId());

        // 2. Buscamos cada concepto en la lista que ya tenemos en memoria.
        schedule.stream()
                .filter(s -> s.getConceptType().getId() == 1L)
                .findFirst()
                .ifPresent(s -> dto.setEnrollmentFeeAmount(s.getInstallmentAmount()));

        schedule.stream()
                .filter(s -> s.getConceptType().getId() == 2L)
                .findFirst()
                .ifPresent(s -> dto.setMonthlyFeeAmount(s.getInstallmentAmount()));

        schedule.stream()
                .filter(s -> s.getConceptType().getId() == 3L)
                .findFirst()
                .ifPresent(s -> dto.setFinalRightsAmount(s.getInstallmentAmount()));

        return dto;
    }

    // ... el resto de la clase no necesita cambios ...
    @Transactional(readOnly = true)
    public List<EnrollmentDto> findAll() {
        return enrollmentRepository.findAllByActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDto create(CreateEnrollmentRequest request) {
        StudentEntity studentEntity = studentRepository.findById(request.getIdStudent())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.getIdStudent()));
        AgentEntity agentEntity = agentRepository.findById(request.getIdAgent())
                .orElseThrow(() -> new ResourceNotFoundException("Agente no encontrado con id: " + request.getIdAgent()));
        CourseEntity courseEntity = courseRepository.findById(request.getIdCourse())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + request.getIdCourse()));
        InstitutionEntity institutionEntity = institutionRepository.findById(request.getIdInstitution())
                .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada con id: " + request.getIdInstitution()));

        CourseInstitutionEntity courseOffering = courseEntity.getInstitutions().stream()
                .filter(ci -> ci.getInstitution().getId().equals(request.getIdInstitution()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("El curso '" + courseEntity.getName() + "' no es ofrecido por la institución '" + institutionEntity.getName() + "'."));

        BigDecimal coursePriceForInstitution = courseOffering.getPrice();
        Integer durationForSchedule = courseOffering.getDurationInMonths();

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

        if (durationForSchedule == null || durationForSchedule <= 0) {
            throw new IllegalStateException("El curso debe tener una duración en meses válida para esta institución.");
        }

        generatePaymentScheduleForEnrollment(savedEntity, coursePriceForInstitution, durationForSchedule,
                request.getEnrollmentDate(), request.getEnrollmentFeeAmount(), request.getFinalRightsAmount());

        return convertToDto(savedEntity);
    }

    private void generatePaymentScheduleForEnrollment(EnrollmentEntity enrollment, BigDecimal coursePrice, int durationInMonths, Instant enrollmentDate, BigDecimal enrollmentFeeAmount, BigDecimal finalRightsAmount) {
        ConceptTypeEntity enrollmentConcept = conceptTypeRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Tipo de Concepto 'Matrícula' no encontrado"));
        ConceptTypeEntity monthlyFeeConcept = conceptTypeRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("Tipo de Concepto 'Cuota Mensual' no encontrado"));
        ConceptTypeEntity finalRightsConcept = conceptTypeRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Tipo de Concepto 'Derechos Finales' no encontrado"));
        InstallmentStatusEntity pendingStatus = installmentStatusRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Estado de cuota 'Pendiente' no encontrado"));
        ZonedDateTime enrollmentZonedDateTime = enrollmentDate.atZone(ZoneOffset.UTC);

        PaymentScheduleEntity enrollmentFee = new PaymentScheduleEntity();
        enrollmentFee.setEnrollment(enrollment);
        enrollmentFee.setConceptType(enrollmentConcept);
        enrollmentFee.setInstallmentAmount(enrollmentFeeAmount);
        enrollmentFee.setInstallmentDueDate(enrollmentDate);
        enrollmentFee.setInstallmentStatus(pendingStatus);
        paymentScheduleRepository.save(enrollmentFee);

        BigDecimal monthlyInstallmentAmount = coursePrice.divide(new BigDecimal(durationInMonths), 2, RoundingMode.HALF_UP);
        for (int i = 1; i <= durationInMonths; i++) {
            PaymentScheduleEntity monthlyFee = new PaymentScheduleEntity();
            monthlyFee.setEnrollment(enrollment);
            monthlyFee.setConceptType(monthlyFeeConcept);
            monthlyFee.setInstallmentAmount(monthlyInstallmentAmount);
            ZonedDateTime nextDueDate = enrollmentZonedDateTime.plusMonths(i);
            monthlyFee.setInstallmentDueDate(nextDueDate.toInstant());
            monthlyFee.setInstallmentStatus(pendingStatus);
            paymentScheduleRepository.save(monthlyFee);
        }

        PaymentScheduleEntity finalRightsFee = new PaymentScheduleEntity();
        finalRightsFee.setEnrollment(enrollment);
        finalRightsFee.setConceptType(finalRightsConcept);
        finalRightsFee.setInstallmentAmount(finalRightsAmount);
        ZonedDateTime finalRightsDueDate = enrollmentZonedDateTime.plusMonths(durationInMonths + 1);
        finalRightsFee.setInstallmentDueDate(finalRightsDueDate.toInstant());
        finalRightsFee.setInstallmentStatus(pendingStatus);
        paymentScheduleRepository.save(finalRightsFee);
    }

    @Transactional(readOnly = true)
    public FullEnrollmentDetailsDto getFullEnrollmentDetails(Long enrollmentId) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada"));
        List<PaymentScheduleEntity> scheduleEntities = paymentScheduleRepository.findByEnrollmentId(enrollmentId);
        List<ScheduledPaymentDto> scheduleDtos = scheduleEntities.stream().map(schedule -> {
            ScheduledPaymentDto scheduleDto = new ScheduledPaymentDto();
            // TODO: Mapear campos de schedule a scheduleDto
            List<PaymentEntity> payments = paymentRepository.findByPaymentScheduleId(schedule.getId());
            List<PaymentDetailDto> paymentDtos = payments.stream().map(payment -> {
                PaymentDetailDto paymentDto = new PaymentDetailDto();
                // TODO: Mapear campos de payment a paymentDto
                return paymentDto;
            }).collect(Collectors.toList());
            scheduleDto.setPaymentsMade(paymentDtos);
            return scheduleDto;
        }).collect(Collectors.toList());
        FullEnrollmentDetailsDto fullDetails = new FullEnrollmentDetailsDto();
        fullDetails.setEnrollmentInfo(convertToDto(enrollment));
        fullDetails.setPaymentSchedule(scheduleDtos);
        return fullDetails;
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
        InstitutionEntity institutionEntity = institutionRepository.findById(request.getIdInstitution())
                .orElseThrow(() -> new ResourceNotFoundException("La institución con id " + request.getIdInstitution() + " no fue encontrada..."));

        List<PaymentScheduleEntity> oldSchedule = paymentScheduleRepository.findByEnrollmentId(id);
        if (!oldSchedule.isEmpty()) {
            paymentRepository.deleteAllByPaymentScheduleIn(oldSchedule);
        }
        paymentScheduleRepository.deleteAll(oldSchedule);

        CourseInstitutionEntity courseOffering = courseEntity.getInstitutions().stream()
                .filter(ci -> ci.getInstitution().getId().equals(request.getIdInstitution()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("El curso '" + courseEntity.getName() + "' no es ofrecido por la institución '" + institutionEntity.getName() + "'."));

        BigDecimal coursePriceForInstitution = courseOffering.getPrice();
        Integer durationForSchedule = courseOffering.getDurationInMonths();

        BigDecimal totalCost = request.getEnrollmentFeeAmount()
                .add(coursePriceForInstitution)
                .add(request.getFinalRightsAmount());

        enrollmentEntity.setTotalEnrollmentCost(totalCost);
        enrollmentEntity.setEnrollmentDate(request.getEnrollmentDate());
        enrollmentEntity.setStudent(studentEntity);
        enrollmentEntity.setAgent(agentEntity);
        enrollmentEntity.setCourse(courseEntity);
        enrollmentEntity.setInstitution(institutionEntity);

        EnrollmentEntity updatedEntity = enrollmentRepository.save(enrollmentEntity);

        generatePaymentScheduleForEnrollment(updatedEntity, coursePriceForInstitution, durationForSchedule,
                request.getEnrollmentDate(), request.getEnrollmentFeeAmount(), request.getFinalRightsAmount());

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
        EnrollmentEntity enrollmentEntity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La matrícula con id " + id + " no fue encontrada."));

        enrollmentEntity.setActive(false);
        enrollmentRepository.save(enrollmentEntity);

        List<PaymentScheduleEntity> schedules = paymentScheduleRepository.findByEnrollmentId(id);
        if (!schedules.isEmpty()) {
            schedules.forEach(schedule -> schedule.setActive(false));
            paymentScheduleRepository.saveAll(schedules);

            List<Long> scheduleIds = schedules.stream().map(PaymentScheduleEntity::getId).collect(Collectors.toList());
            List<PaymentEntity> payments = paymentRepository.findByPaymentScheduleIdIn(scheduleIds);
            if (!payments.isEmpty()) {
                payments.forEach(payment -> payment.setActive(false));
                paymentRepository.saveAll(payments);
            }
        }
    }

    @Transactional
    public void activateById(Long id) {
        EnrollmentEntity enrollmentEntity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La matrícula con id " + id + " no fue encontrada."));

        enrollmentEntity.setActive(true);
        enrollmentRepository.save(enrollmentEntity);

        List<PaymentScheduleEntity> schedules = paymentScheduleRepository.findAllByEnrollmentIdAndActiveFalse(id);
        for (PaymentScheduleEntity schedule : schedules) {
            paymentScheduleService.activateById(schedule.getId());
        }
    }
}


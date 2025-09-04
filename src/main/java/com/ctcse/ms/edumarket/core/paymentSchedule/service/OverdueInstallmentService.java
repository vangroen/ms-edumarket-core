package com.ctcse.ms.edumarket.core.paymentSchedule.service;

import com.ctcse.ms.edumarket.core.installmentStatus.entity.InstallmentStatusEntity;
import com.ctcse.ms.edumarket.core.installmentStatus.repository.InstallmentStatusRepository;
import com.ctcse.ms.edumarket.core.paymentSchedule.entity.PaymentScheduleEntity;
import com.ctcse.ms.edumarket.core.paymentSchedule.repository.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OverdueInstallmentService {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final InstallmentStatusRepository installmentStatusRepository;
    private static final Logger logger = LoggerFactory.getLogger(OverdueInstallmentService.class);

    // Se ejecuta todos los días a la 1:00 AM
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void updateOverdueInstallments() {
        logger.info("Iniciando tarea programada: Actualización de cuotas vencidas...");

        // Buscamos el estado "Vencida" (ID=3)
        InstallmentStatusEntity overdueStatus = installmentStatusRepository.findById(3L)
                .orElse(null);

        if (overdueStatus == null) {
            logger.error("El estado 'Vencida' no se encontró en la base de datos. La tarea no puede continuar.");
            return;
        }

        // Buscamos todas las cuotas pendientes cuya fecha ya pasó
        List<PaymentScheduleEntity> overdueInstallments = paymentScheduleRepository.findOverdueInstallments(Instant.now());

        if (overdueInstallments.isEmpty()) {
            logger.info("No se encontraron cuotas vencidas para actualizar.");
            return;
        }

        logger.info("Se encontraron {} cuotas vencidas. Actualizando estados...", overdueInstallments.size());

        for (PaymentScheduleEntity installment : overdueInstallments) {
            installment.setInstallmentStatus(overdueStatus);
            paymentScheduleRepository.save(installment);
        }

        logger.info("Tarea finalizada. Se actualizaron {} cuotas al estado 'Vencida'.", overdueInstallments.size());
    }
}

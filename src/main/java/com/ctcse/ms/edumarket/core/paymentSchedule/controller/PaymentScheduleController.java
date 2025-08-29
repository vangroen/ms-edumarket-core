package com.ctcse.ms.edumarket.core.paymentSchedule.controller;

import com.ctcse.ms.edumarket.core.paymentSchedule.dto.CreatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.PaymentScheduleDto;
import com.ctcse.ms.edumarket.core.paymentSchedule.dto.UpdatePaymentScheduleRequest;
import com.ctcse.ms.edumarket.core.paymentSchedule.service.PaymentScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments-schedules")
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final PaymentScheduleService service;

    @GetMapping
    public ResponseEntity<List<PaymentScheduleDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<PaymentScheduleDto> create(@Valid @RequestBody CreatePaymentScheduleRequest request) {
        PaymentScheduleDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentScheduleDto> update(@PathVariable Long id, @Valid @RequestBody UpdatePaymentScheduleRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentScheduleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // --- ENDPOINT NUEVO: ELIMINAR (CON MENSAJE DE ÉXITO) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El cronograma de pago con id " + id + " ha sido eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}

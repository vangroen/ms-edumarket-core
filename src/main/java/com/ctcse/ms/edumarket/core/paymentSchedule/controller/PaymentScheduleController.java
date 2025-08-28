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

import java.util.List;

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
}

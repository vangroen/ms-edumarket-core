package com.ctcse.ms.edumarket.core.payment.controller;

import com.ctcse.ms.edumarket.core.payment.dto.CreatePaymentRequest;
import com.ctcse.ms.edumarket.core.payment.dto.PaymentDto;
import com.ctcse.ms.edumarket.core.payment.service.PaymentService;
import com.ctcse.ms.edumarket.core.person.dto.CreatePersonRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }
}

package com.ctcse.ms.edumarket.core.payment.controller;

import com.ctcse.ms.edumarket.core.payment.dto.CreatePaymentRequest;
import com.ctcse.ms.edumarket.core.payment.dto.PaymentDto;
import com.ctcse.ms.edumarket.core.payment.dto.UpdatePaymentRequest;
import com.ctcse.ms.edumarket.core.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
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

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable Long id, @Valid @RequestBody UpdatePaymentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El pago con id " + id + " ha sido eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}

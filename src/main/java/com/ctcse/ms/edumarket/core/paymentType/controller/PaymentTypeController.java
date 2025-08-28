package com.ctcse.ms.edumarket.core.paymentType.controller;

import com.ctcse.ms.edumarket.core.paymentType.dto.CreatePaymentTypeRequest;
import com.ctcse.ms.edumarket.core.paymentType.dto.PaymentTypeDto;
import com.ctcse.ms.edumarket.core.paymentType.dto.UpdatePaymentTypeRequest;
import com.ctcse.ms.edumarket.core.paymentType.service.PaymentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment-type")
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService service;

    @GetMapping
    public ResponseEntity<List<PaymentTypeDto>> getAll() {
        List<PaymentTypeDto> dtos = service.findAll();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<PaymentTypeDto> create(@Valid @RequestBody CreatePaymentTypeRequest request) {
        PaymentTypeDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTypeDto> update(@PathVariable Long id, @Valid @RequestBody UpdatePaymentTypeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "El tipo de pago con id " + id + " ha sido eliminado exitosamente.");

        return ResponseEntity.ok(response);
    }
}

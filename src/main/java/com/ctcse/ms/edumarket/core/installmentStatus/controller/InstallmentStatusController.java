package com.ctcse.ms.edumarket.core.installmentStatus.controller;

import com.ctcse.ms.edumarket.core.installmentStatus.dto.CreateInstallmentStatusRequest;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.UpdateInstallmentStatusRequest;
import com.ctcse.ms.edumarket.core.installmentStatus.service.InstallmentStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/installment-status")
@RequiredArgsConstructor
public class InstallmentStatusController {

    private final InstallmentStatusService service;

    @GetMapping
    public ResponseEntity<List<InstallmentStatusDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<InstallmentStatusDto> create(@Valid @RequestBody CreateInstallmentStatusRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstallmentStatusDto> update(@PathVariable Long id, @Valid @RequestBody UpdateInstallmentStatusRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallmentStatusDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El estado de cuota con id " + id + " ha sido eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}

package com.ctcse.ms.edumarket.core.installmentStatus.controller;

import com.ctcse.ms.edumarket.core.installmentStatus.dto.CreateInstallmentStatusRequest;
import com.ctcse.ms.edumarket.core.installmentStatus.dto.InstallmentStatusDto;
import com.ctcse.ms.edumarket.core.installmentStatus.service.InstallmentStatusService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

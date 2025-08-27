package com.ctcse.ms.edumarket.core.enrollment.controller;

import com.ctcse.ms.edumarket.core.enrollment.dto.CreateEnrollmentRequest;
import com.ctcse.ms.edumarket.core.enrollment.dto.EnrollmentDto;
import com.ctcse.ms.edumarket.core.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService service;

    @GetMapping
    public ResponseEntity<List<EnrollmentDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> create(@Valid @RequestBody CreateEnrollmentRequest request) {
        EnrollmentDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }
}

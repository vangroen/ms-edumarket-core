package com.ctcse.ms.edumarket.core.enrollment.controller;

import com.ctcse.ms.edumarket.core.enrollment.dto.CreateEnrollmentRequest;
import com.ctcse.ms.edumarket.core.enrollment.dto.EnrollmentDto;
import com.ctcse.ms.edumarket.core.enrollment.dto.UpdateEnrollmentRequest;
import com.ctcse.ms.edumarket.core.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDto> update(@PathVariable Long id, @Valid @RequestBody UpdateEnrollmentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La matrícula con id " + id + " ha sido eliminada exitosamente.");
        return ResponseEntity.ok(response);
    }
}

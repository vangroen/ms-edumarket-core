package com.ctcse.ms.edumarket.core.institution.controller;

import com.ctcse.ms.edumarket.core.institution.dto.CreateInstitutionRequest;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.institution.dto.UpdateInstitutionRequest;
import com.ctcse.ms.edumarket.core.institution.service.InstitutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService service;

    @GetMapping
    public ResponseEntity<List<InstitutionDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<InstitutionDto> create(@Valid @RequestBody CreateInstitutionRequest request) {
        InstitutionDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDto> update(@PathVariable Long id, @Valid @RequestBody UpdateInstitutionRequest request) {
        InstitutionDto updatedDto = service.update(id, request);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La institución con id " + id + " ha sido eliminada exitosamente.");
        return ResponseEntity.ok(response);
    }
}

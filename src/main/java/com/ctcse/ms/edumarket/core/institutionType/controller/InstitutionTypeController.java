package com.ctcse.ms.edumarket.core.institutionType.controller;

import com.ctcse.ms.edumarket.core.institutionType.dto.CreateInstitutionTypeRequest;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.institutionType.dto.UpdateInstitutionTypeRequest;
import com.ctcse.ms.edumarket.core.institutionType.service.InstitutionTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/institution-type")
@RequiredArgsConstructor
public class InstitutionTypeController {

    private final InstitutionTypeService service;

    @GetMapping
    public ResponseEntity<List<InstitutionTypeDto>> getAll() {
        List<InstitutionTypeDto> dtos = service.findAll();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<InstitutionTypeDto> create(@Valid @RequestBody CreateInstitutionTypeRequest request) {
        InstitutionTypeDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionTypeDto> update(@PathVariable Long id, @Valid @RequestBody UpdateInstitutionTypeRequest request) {
        InstitutionTypeDto updatedDto = service.update(id, request);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionTypeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "El tipo de institución con id " + id + " ha sido eliminado exitosamente.");

        return ResponseEntity.ok(response);
    }
}

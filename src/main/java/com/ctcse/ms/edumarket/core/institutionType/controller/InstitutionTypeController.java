package com.ctcse.ms.edumarket.core.institutionType.controller;

import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.institutionType.dto.CreateInstitutionTypeRequest;
import com.ctcse.ms.edumarket.core.institutionType.dto.InstitutionTypeDto;
import com.ctcse.ms.edumarket.core.institutionType.service.InstitutionTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

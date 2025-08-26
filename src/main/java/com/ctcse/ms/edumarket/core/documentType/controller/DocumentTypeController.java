package com.ctcse.ms.edumarket.core.documentType.controller;

import com.ctcse.ms.edumarket.core.documentType.dto.CreateDocumentTypeRequest;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.documentType.service.DocumentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document-type")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final DocumentTypeService service;

    @GetMapping
    public ResponseEntity<List<DocumentTypeDto>> getAll() {
        List<DocumentTypeDto> dtos = service.findAll();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<DocumentTypeDto> create(@Valid @RequestBody CreateDocumentTypeRequest request) {
        DocumentTypeDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }
}

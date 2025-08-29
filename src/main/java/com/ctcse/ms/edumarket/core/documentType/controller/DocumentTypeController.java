package com.ctcse.ms.edumarket.core.documentType.controller;

import com.ctcse.ms.edumarket.core.documentType.dto.CreateDocumentTypeRequest;
import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import com.ctcse.ms.edumarket.core.documentType.dto.UpdateDocumentTypeRequest;
import com.ctcse.ms.edumarket.core.documentType.service.DocumentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}")
    public ResponseEntity<DocumentTypeDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentTypeRequest request) {
        DocumentTypeDto updatedDto = service.update(id, request);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El tipo de documento con id " + id + " ha sido eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}

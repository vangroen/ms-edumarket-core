package com.ctcse.ms.edumarket.core.conceptType.controller;

import com.ctcse.ms.edumarket.core.conceptType.dto.ConceptTypeDto;
import com.ctcse.ms.edumarket.core.conceptType.dto.CreateConceptTypeRequest;
import com.ctcse.ms.edumarket.core.conceptType.dto.UpdateConceptTypeRequest;
import com.ctcse.ms.edumarket.core.conceptType.entity.ConceptTypeEntity;
import com.ctcse.ms.edumarket.core.conceptType.service.ConceptTypeService;
import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concept-type")
@RequiredArgsConstructor
public class ConceptTypeController {

    private final ConceptTypeService service;

    @GetMapping
    public ResponseEntity<List<ConceptTypeDto>> getAll() {
        List<ConceptTypeDto> entities = service.findAll();
        return ResponseEntity.ok(entities);
    }

    @PostMapping
    public ResponseEntity<ConceptTypeDto> create(@Valid @RequestBody CreateConceptTypeRequest request) {
        ConceptTypeDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConceptTypeDto> update(@PathVariable Long id, @Valid @RequestBody UpdateConceptTypeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}

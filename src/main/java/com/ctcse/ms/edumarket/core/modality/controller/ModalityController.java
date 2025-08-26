package com.ctcse.ms.edumarket.core.modality.controller;

import com.ctcse.ms.edumarket.core.modality.dto.CreateModalityRequest;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.modality.service.ModalityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modality")
@RequiredArgsConstructor
public class ModalityController {

    private final ModalityService service;

    @GetMapping
    public ResponseEntity<List<ModalityDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<ModalityDto> create(@Valid @RequestBody CreateModalityRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }
}

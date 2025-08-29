package com.ctcse.ms.edumarket.core.profession.controller;

import com.ctcse.ms.edumarket.core.profession.dto.CreateProfessionRequest;
import com.ctcse.ms.edumarket.core.profession.dto.ProfessionDto;
import com.ctcse.ms.edumarket.core.profession.dto.UpdateProfessionRequest;
import com.ctcse.ms.edumarket.core.profession.service.ProfessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/profession")
@RequiredArgsConstructor
public class ProfessionController {

    private final ProfessionService service;

    @GetMapping
    public ResponseEntity<List<ProfessionDto>> getAll() {
        List<ProfessionDto> dtos = service.findAll();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ProfessionDto> create(@Valid @RequestBody CreateProfessionRequest request) {
        ProfessionDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionDto> update(@PathVariable Long id, @Valid @RequestBody UpdateProfessionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La profesión con id " + id + " ha sido eliminada exitosamente.");
        return ResponseEntity.ok(response);
    }
}

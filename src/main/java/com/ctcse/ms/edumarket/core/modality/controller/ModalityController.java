package com.ctcse.ms.edumarket.core.modality.controller;

import com.ctcse.ms.edumarket.core.modality.dto.CreateModalityRequest;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import com.ctcse.ms.edumarket.core.modality.dto.UpdateModalityRequest;
import com.ctcse.ms.edumarket.core.modality.service.ModalityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}")
    public ResponseEntity<ModalityDto> update(@PathVariable Long id, @Valid @RequestBody UpdateModalityRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModalityDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // --- ENDPOINT NUEVO: ELIMINAR (CON MENSAJE DE ÉXITO) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La modalidad con id " + id + " ha sido eliminada exitosamente.");
        return ResponseEntity.ok(response);
    }
}

package com.ctcse.ms.edumarket.core.academicRank.controller;

import com.ctcse.ms.edumarket.core.academicRank.dto.AcademicRankDto;
import com.ctcse.ms.edumarket.core.academicRank.dto.CreateAcademicRankRequest;
import com.ctcse.ms.edumarket.core.academicRank.dto.UpdateAcademicRankRequest;
import com.ctcse.ms.edumarket.core.academicRank.service.AcademicRankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/academic-rank")
@RequiredArgsConstructor
public class AcademicRankController {

    private final AcademicRankService service;

    @GetMapping
    public ResponseEntity<List<AcademicRankDto>> getAll() {
        List<AcademicRankDto> dtos = service.findAll();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<AcademicRankDto> create(@Valid @RequestBody CreateAcademicRankRequest request) {
        AcademicRankDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicRankDto> update(@PathVariable Long id, @Valid @RequestBody UpdateAcademicRankRequest request) {
        AcademicRankDto updatedDto = service.update(id, request);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicRankDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "El rango académico con id " + id + " ha sido eliminado exitosamente.");

        return ResponseEntity.ok(response);
    }
}

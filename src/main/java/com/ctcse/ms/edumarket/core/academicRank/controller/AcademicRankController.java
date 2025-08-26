package com.ctcse.ms.edumarket.core.academicRank.controller;

import com.ctcse.ms.edumarket.core.academicRank.dto.AcademicRankDto;
import com.ctcse.ms.edumarket.core.academicRank.dto.CreateAcademicRankRequest;
import com.ctcse.ms.edumarket.core.academicRank.service.AcademicRankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

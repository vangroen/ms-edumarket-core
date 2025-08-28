package com.ctcse.ms.edumarket.core.student.controller;

import com.ctcse.ms.edumarket.core.student.dto.CreateStudentRequest;
import com.ctcse.ms.edumarket.core.student.dto.StudentDto;
import com.ctcse.ms.edumarket.core.student.dto.UpdateStudentRequest;
import com.ctcse.ms.edumarket.core.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<StudentDto> create(@Valid @RequestBody CreateStudentRequest request) {
        StudentDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> update(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}

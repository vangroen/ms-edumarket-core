package com.ctcse.ms.edumarket.core.courseType.controller;

import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.dto.CreateCourseTypeRequest;
import com.ctcse.ms.edumarket.core.courseType.dto.UpdateCourseTypeRequest;
import com.ctcse.ms.edumarket.core.courseType.service.CourseTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/course-type")
@RequiredArgsConstructor
public class CourseTypeController {

    private final CourseTypeService service;

    @GetMapping
    public ResponseEntity<List<CourseTypeDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<CourseTypeDto> create(@Valid @RequestBody CreateCourseTypeRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseTypeDto> update(@PathVariable Long id, @Valid @RequestBody UpdateCourseTypeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseTypeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El tipo de curso con id " + id + " ha sido eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}

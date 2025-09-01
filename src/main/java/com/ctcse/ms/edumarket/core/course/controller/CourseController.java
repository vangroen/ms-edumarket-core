package com.ctcse.ms.edumarket.core.course.controller;

import com.ctcse.ms.edumarket.core.course.dto.CourseDto;
import com.ctcse.ms.edumarket.core.course.dto.CourseWithInstitutionsDto;
import com.ctcse.ms.edumarket.core.course.dto.CreateCourseRequest;
import com.ctcse.ms.edumarket.core.course.dto.UpdateCourseRequest;
import com.ctcse.ms.edumarket.core.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @GetMapping("/courses-by-institution")
    public ResponseEntity<List<CourseWithInstitutionsDto>> getAllCoursesByInstitution() {
        return ResponseEntity.ok(service.findAllCoursesWithInstitutions());
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseDto> create(@Valid @RequestBody CreateCourseRequest request) {
        CourseDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseDto> update(@PathVariable Long id, @Valid @RequestBody UpdateCourseRequest request) {
        CourseDto updatedDto = service.update(id, request);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "El curso con id " + id + " ha sido eliminado exitosamente.");
        return ResponseEntity.ok(response);
    }
}

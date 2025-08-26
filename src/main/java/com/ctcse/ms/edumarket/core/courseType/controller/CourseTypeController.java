package com.ctcse.ms.edumarket.core.courseType.controller;

import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.courseType.dto.CreateCourseTypeRequest;
import com.ctcse.ms.edumarket.core.courseType.service.CourseTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

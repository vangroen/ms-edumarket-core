package com.ctcse.ms.edumarket.core.person.controller;

import com.ctcse.ms.edumarket.core.person.dto.CreatePersonRequest;
import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import com.ctcse.ms.edumarket.core.person.dto.UpdatePersonRequest;
import com.ctcse.ms.edumarket.core.person.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<PersonDto> create(@Valid @RequestBody CreatePersonRequest request) {
        PersonDto newDto = service.create(request);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable Long id, @Valid @RequestBody UpdatePersonRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La persona con id " + id + " ha sido eliminada exitosamente.");
        return ResponseEntity.ok(response);
    }
}

package com.example.scheme.controller;

import com.example.scheme.dto.SchemeRequest;
import com.example.scheme.model.Scheme;
import com.example.scheme.service.SchemeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// REST Controller handling retrieval and creation of government schemes and their rules.
@RestController
@RequestMapping("/schemes")
public class SchemeController {
    private final SchemeService schemeService;

    public SchemeController(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    @GetMapping
    public List<Scheme> getAllSchemes() {
        return schemeService.getAllSchemes();
    }

    @GetMapping("/{id}")
    public Scheme getSchemeById(@PathVariable Long id) {
        return schemeService.getSchemeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Scheme addScheme(@Valid @RequestBody SchemeRequest request) {
        return schemeService.createScheme(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
    }
}

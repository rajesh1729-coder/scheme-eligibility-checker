package com.example.scheme.controller;

import com.example.scheme.dto.UserProfile;
import com.example.scheme.model.Scheme;
import com.example.scheme.service.SchemeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// REST Controller exposing the endpoint to check scheme eligibility for a given citizen profile.
@RestController
@RequestMapping("/eligibility")
public class EligibilityController {
    private final SchemeService schemeService;

    public EligibilityController(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    @PostMapping("/check")
    public List<Scheme> checkEligibility(@Valid @RequestBody UserProfile userProfile) {
        return schemeService.checkEligibility(userProfile);
    }
}

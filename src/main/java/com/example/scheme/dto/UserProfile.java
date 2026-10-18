package com.example.scheme.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Data Transfer Object encapsulating validated citizen demographic data for eligibility evaluation.
public record UserProfile(
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    Integer age,

    @NotNull(message = "Annual income is required")
    @Min(value = 0, message = "Annual income cannot be negative")
    Double annualIncome,

    @NotBlank(message = "State is required")
    String state,

    @NotBlank(message = "Category is required")
    String category,

    @NotBlank(message = "Occupation is required")
    String occupation
) {}

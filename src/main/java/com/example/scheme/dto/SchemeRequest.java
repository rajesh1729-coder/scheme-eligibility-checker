package com.example.scheme.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

// Data Transfer Object for creating a new government scheme along with its list of rules via POST.
public record SchemeRequest(
    @NotBlank(message = "Scheme name is required") String name,
    @NotBlank(message = "Description is required") String description,
    @NotBlank(message = "Benefit is required") String benefit,
    String applyLink,
    String sourceUrl,
    @NotEmpty(message = "At least one rule is required") @Valid List<RuleDto> rules
) {
    public record RuleDto(
        @NotBlank(message = "Field is required") String field,
        @NotBlank(message = "Operator is required") String operator,
        @NotBlank(message = "Value is required") String value
    ) {}
}

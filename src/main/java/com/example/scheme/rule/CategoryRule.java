package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;
import java.util.Arrays;

// Strategy implementation that evaluates whether the citizen's social category matches allowed categories.
public class CategoryRule implements Rule {
    private static final java.util.Set<String> VALID_OPERATORS = java.util.Set.of("EQ", "==", "IN");

    private final String operator;
    private final String targetCategory;

    public CategoryRule(String operator, String value) {
        if (operator == null || operator.trim().isBlank()) {
            throw new IllegalArgumentException("Category operator cannot be null or blank");
        }
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("Category value cannot be null or blank");
        }
        this.operator = operator.trim().toUpperCase();
        this.targetCategory = value.trim();
        if (!"ALL".equalsIgnoreCase(this.targetCategory) && !VALID_OPERATORS.contains(this.operator)) {
            throw new IllegalArgumentException("Unsupported category operator: " + operator);
        }
    }

    @Override
    public boolean isSatisfiedBy(UserProfile user) {
        if ("ALL".equalsIgnoreCase(targetCategory)) return true;
        if (user == null || user.category() == null) return false;
        return switch (operator) {
            case "EQ", "==" -> targetCategory.equalsIgnoreCase(user.category().trim());
            case "IN" -> Arrays.stream(targetCategory.split(","))
                    .map(String::trim)
                    .anyMatch(c -> c.equalsIgnoreCase(user.category().trim()));
            default -> throw new IllegalArgumentException("Unsupported category operator: " + operator);
        };
    }
}

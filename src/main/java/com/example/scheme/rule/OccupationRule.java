package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;
import java.util.Arrays;
import java.util.Set;

// Strategy implementation that evaluates whether the citizen's occupation matches allowed occupations.
public class OccupationRule implements Rule {
    private static final Set<String> VALID_OPERATORS = Set.of("EQ", "==", "IN");

    private final String operator;
    private final String targetOccupation;

    public OccupationRule(String operator, String value) {
        if (operator == null || operator.trim().isBlank()) {
            throw new IllegalArgumentException("Occupation operator cannot be null or blank");
        }
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("Occupation value cannot be null or blank");
        }
        this.operator = operator.trim().toUpperCase();
        this.targetOccupation = value.trim();

        if (!"ALL".equalsIgnoreCase(this.targetOccupation) && !VALID_OPERATORS.contains(this.operator)) {
            throw new IllegalArgumentException("Unsupported occupation operator: " + operator);
        }
    }

    @Override
    public boolean isSatisfiedBy(UserProfile user) {
        if ("ALL".equalsIgnoreCase(targetOccupation)) {
            return true;
        }
        if (user == null || user.occupation() == null) {
            return false;
        }
        return switch (operator) {
            case "EQ", "==" -> targetOccupation.equalsIgnoreCase(user.occupation().trim());
            case "IN" -> Arrays.stream(targetOccupation.split(","))
                    .map(String::trim)
                    .anyMatch(occ -> occ.equalsIgnoreCase(user.occupation().trim()));
            default -> throw new IllegalArgumentException("Unsupported occupation operator: " + operator);
        };
    }
}

package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;
import java.util.Arrays;

// Strategy implementation that checks if the citizen resides in an eligible state (or ALL for central schemes).
public class StateRule implements Rule {
    private static final java.util.Set<String> VALID_OPERATORS = java.util.Set.of("EQ", "==", "IN");

    private final String operator;
    private final String targetState;

    public StateRule(String operator, String value) {
        if (operator == null || operator.trim().isBlank()) {
            throw new IllegalArgumentException("State operator cannot be null or blank");
        }
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("State value cannot be null or blank");
        }
        this.operator = operator.trim().toUpperCase();
        this.targetState = value.trim();
        if (!"ALL".equalsIgnoreCase(this.targetState) && !VALID_OPERATORS.contains(this.operator)) {
            throw new IllegalArgumentException("Unsupported state operator: " + operator);
        }
    }

    @Override
    public boolean isSatisfiedBy(UserProfile user) {
        if ("ALL".equalsIgnoreCase(targetState)) return true;
        if (user == null || user.state() == null) return false;
        return switch (operator) {
            case "EQ", "==" -> targetState.equalsIgnoreCase(user.state().trim());
            case "IN" -> Arrays.stream(targetState.split(","))
                    .map(String::trim)
                    .anyMatch(s -> s.equalsIgnoreCase(user.state().trim()));
            default -> throw new IllegalArgumentException("Unsupported state operator: " + operator);
        };
    }
}

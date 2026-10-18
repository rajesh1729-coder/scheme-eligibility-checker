package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;

// Strategy implementation that evaluates citizen age against numeric comparison operators.
public class AgeRule implements Rule {
    private static final java.util.Set<String> VALID_OPERATORS = java.util.Set.of("GTE", ">=", "LTE", "<=", "GT", ">", "LT", "<", "EQ", "==");

    private final String operator;
    private final int targetAge;

    public AgeRule(String operator, String value) {
        if (operator == null || operator.trim().isBlank()) {
            throw new IllegalArgumentException("Age operator cannot be null or blank");
        }
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("Age value cannot be null or blank");
        }
        this.operator = operator.trim().toUpperCase();
        if (!VALID_OPERATORS.contains(this.operator)) {
            throw new IllegalArgumentException("Unsupported age operator: " + operator);
        }
        try {
            this.targetAge = Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid age value: " + value, ex);
        }
    }

    @Override
    public boolean isSatisfiedBy(UserProfile user) {
        if (user == null || user.age() == null) {
            return false;
        }
        int age = user.age();
        return switch (operator) {
            case "GTE", ">=" -> age >= targetAge;
            case "LTE", "<=" -> age <= targetAge;
            case "GT", ">"   -> age > targetAge;
            case "LT", "<"   -> age < targetAge;
            case "EQ", "=="  -> age == targetAge;
            default -> throw new IllegalArgumentException("Unsupported age operator: " + operator);
        };
    }
}

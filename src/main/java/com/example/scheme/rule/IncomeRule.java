package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;

// Strategy implementation that checks whether the citizen's annual income satisfies the threshold.
public class IncomeRule implements Rule {
    private static final java.util.Set<String> VALID_OPERATORS = java.util.Set.of("LTE", "<=", "LT", "<", "GTE", ">=", "GT", ">", "EQ", "==");

    private final String operator;
    private final double targetIncome;

    public IncomeRule(String operator, String value) {
        if (operator == null || operator.trim().isBlank()) {
            throw new IllegalArgumentException("Income operator cannot be null or blank");
        }
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("Income value cannot be null or blank");
        }
        this.operator = operator.trim().toUpperCase();
        if (!VALID_OPERATORS.contains(this.operator)) {
            throw new IllegalArgumentException("Unsupported income operator: " + operator);
        }
        try {
            this.targetIncome = Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid income value: " + value, ex);
        }
    }

    @Override
    public boolean isSatisfiedBy(UserProfile user) {
        if (user == null || user.annualIncome() == null) {
            return false;
        }
        double income = user.annualIncome();
        return switch (operator) {
            case "LTE", "<=" -> income <= targetIncome;
            case "LT", "<"   -> income < targetIncome;
            case "GTE", ">=" -> income >= targetIncome;
            case "GT", ">"   -> income > targetIncome;
            case "EQ", "=="  -> Double.compare(income, targetIncome) == 0;
            default -> throw new IllegalArgumentException("Unsupported income operator: " + operator);
        };
    }
}

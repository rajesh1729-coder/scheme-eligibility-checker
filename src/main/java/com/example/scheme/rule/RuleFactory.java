package com.example.scheme.rule;

import com.example.scheme.model.SchemeRule;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.BiFunction;

// Factory component that converts a persisted SchemeRule entity into its corresponding Strategy Rule instance.
@Component
public class RuleFactory {
    private final Map<String, BiFunction<String, String, Rule>> registry = Map.of(
        "AGE", AgeRule::new,
        "INCOME", IncomeRule::new,
        "STATE", StateRule::new,
        "CATEGORY", CategoryRule::new,
        "OCCUPATION", OccupationRule::new
    );

    public Rule createRule(SchemeRule schemeRule) {
        if (schemeRule == null || schemeRule.getField() == null || schemeRule.getField().isBlank()) {
            throw new IllegalArgumentException("Rule field cannot be null or empty");
        }
        String fieldKey = schemeRule.getField().trim().toUpperCase();
        BiFunction<String, String, Rule> creator = registry.get(fieldKey);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown rule field type: " + schemeRule.getField());
        }
        return creator.apply(schemeRule.getOperator(), schemeRule.getValue());
    }
}

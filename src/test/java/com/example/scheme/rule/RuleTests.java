package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;
import com.example.scheme.model.SchemeRule;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Unit test suite verifying individual Strategy Rule implementations and the RuleFactory.
class RuleTests {

    private final UserProfile sampleUser = new UserProfile(28, 150000.0, "Maharashtra", "OBC", "Farmer");

    @Test
    void testAgeRuleGteAndLte() {
        assertTrue(new AgeRule("GTE", "18").isSatisfiedBy(sampleUser));
        assertTrue(new AgeRule("LTE", "40").isSatisfiedBy(sampleUser));
        assertFalse(new AgeRule("GTE", "60").isSatisfiedBy(sampleUser));
    }

    @Test
    void testAgeRuleConstructorRejection() {
        assertThrows(IllegalArgumentException.class, () -> new AgeRule("INVALID", "20"));
        assertThrows(IllegalArgumentException.class, () -> new AgeRule("GTE", "not-a-number"));
        assertThrows(IllegalArgumentException.class, () -> new AgeRule("", "20"));
    }

    @Test
    void testIncomeRuleLteAndGt() {
        assertTrue(new IncomeRule("LTE", "200000").isSatisfiedBy(sampleUser));
        assertFalse(new IncomeRule("LTE", "100000").isSatisfiedBy(sampleUser));
        assertTrue(new IncomeRule("GT", "50000").isSatisfiedBy(sampleUser));
    }

    @Test
    void testIncomeRuleConstructorRejection() {
        assertThrows(IllegalArgumentException.class, () -> new IncomeRule("BETWEEN", "100000"));
        assertThrows(IllegalArgumentException.class, () -> new IncomeRule("LTE", "abc"));
    }

    @Test
    void testStateRuleEqAndAll() {
        assertTrue(new StateRule("EQ", "maharashtra").isSatisfiedBy(sampleUser));
        assertTrue(new StateRule("EQ", "ALL").isSatisfiedBy(sampleUser));
        assertFalse(new StateRule("EQ", "Karnataka").isSatisfiedBy(sampleUser));
    }

    @Test
    void testStateRuleConstructorRejection() {
        assertThrows(IllegalArgumentException.class, () -> new StateRule("GT", "Maharashtra"));
    }

    @Test
    void testCategoryRuleEqAndIn() {
        assertTrue(new CategoryRule("EQ", "OBC").isSatisfiedBy(sampleUser));
        assertTrue(new CategoryRule("IN", "SC, ST, OBC").isSatisfiedBy(sampleUser));
        assertFalse(new CategoryRule("EQ", "SC").isSatisfiedBy(sampleUser));
    }

    @Test
    void testCategoryRuleConstructorRejection() {
        assertThrows(IllegalArgumentException.class, () -> new CategoryRule("LTE", "SC"));
    }

    @Test
    void testOccupationRule() {
        assertTrue(new OccupationRule("EQ", "Farmer").isSatisfiedBy(sampleUser));
        assertTrue(new OccupationRule("IN", "Student, Farmer, Worker").isSatisfiedBy(sampleUser));
        assertTrue(new OccupationRule("EQ", "ALL").isSatisfiedBy(sampleUser));
        assertFalse(new OccupationRule("EQ", "Student").isSatisfiedBy(sampleUser));
        assertThrows(IllegalArgumentException.class, () -> new OccupationRule("LTE", "Farmer"));
    }

    @Test
    void testRuleFactoryCreatesCorrectRuleAndRejectsUnknown() {
        RuleFactory factory = new RuleFactory();
        Rule rule = factory.createRule(new SchemeRule("AGE", "GTE", "21"));
        assertInstanceOf(AgeRule.class, rule);
        assertTrue(rule.isSatisfiedBy(sampleUser));

        Rule occRule = factory.createRule(new SchemeRule("OCCUPATION", "EQ", "Farmer"));
        assertInstanceOf(OccupationRule.class, occRule);
        assertTrue(occRule.isSatisfiedBy(sampleUser));

        assertThrows(IllegalArgumentException.class,
                () -> factory.createRule(new SchemeRule("INVALID_FIELD", "EQ", "X")));
        assertThrows(IllegalArgumentException.class,
                () -> factory.createRule(new SchemeRule("", "EQ", "X")));
    }
}

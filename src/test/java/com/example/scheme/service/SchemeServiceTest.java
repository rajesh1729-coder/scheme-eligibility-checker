package com.example.scheme.service;

import com.example.scheme.dto.SchemeRequest;
import com.example.scheme.dto.UserProfile;
import com.example.scheme.exception.ResourceNotFoundException;
import com.example.scheme.model.Scheme;
import com.example.scheme.model.SchemeRule;
import com.example.scheme.repository.SchemeRepository;
import com.example.scheme.rule.RuleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Unit tests verifying SchemeService's eligibility evaluation, fail-fast rule validation, and CRUD operations.
class SchemeServiceTest {

    private SchemeRepository schemeRepository;
    private SchemeService schemeService;

    @BeforeEach
    void setUp() {
        schemeRepository = Mockito.mock(SchemeRepository.class);
        schemeService = new SchemeService(schemeRepository, new RuleFactory());
    }

    private Scheme buildMultiRuleScheme() {
        Scheme scheme = new Scheme(1L, "Test Scheme", "Desc", "Benefit", "http://apply", "http://source");
        scheme.addRule(new SchemeRule("AGE", "GTE", "21"));
        scheme.addRule(new SchemeRule("INCOME", "LTE", "250000"));
        scheme.addRule(new SchemeRule("STATE", "EQ", "Maharashtra"));
        scheme.addRule(new SchemeRule("CATEGORY", "IN", "SC,OBC"));
        scheme.addRule(new SchemeRule("OCCUPATION", "EQ", "Student"));
        return scheme;
    }

    @Test
    void shouldReturnSchemeWhenAllRulesPass() {
        when(schemeRepository.findAll()).thenReturn(List.of(buildMultiRuleScheme()));
        UserProfile eligibleUser = new UserProfile(25, 180000.0, "Maharashtra", "OBC", "Student");

        List<Scheme> result = schemeService.checkEligibility(eligibleUser);
        assertEquals(1, result.size());
        assertEquals("Test Scheme", result.get(0).getName());
    }

    @Test
    void shouldRejectSchemeWhenEvenOneRuleFails_IncomeTooHigh() {
        when(schemeRepository.findAll()).thenReturn(List.of(buildMultiRuleScheme()));
        UserProfile richUser = new UserProfile(25, 300000.0, "Maharashtra", "OBC", "Student");

        List<Scheme> result = schemeService.checkEligibility(richUser);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRejectSchemeWhenEvenOneRuleFails_WrongState() {
        when(schemeRepository.findAll()).thenReturn(List.of(buildMultiRuleScheme()));
        UserProfile otherStateUser = new UserProfile(25, 180000.0, "Gujarat", "OBC", "Student");

        List<Scheme> result = schemeService.checkEligibility(otherStateUser);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRejectSchemeWhenEvenOneRuleFails_WrongOccupation() {
        when(schemeRepository.findAll()).thenReturn(List.of(buildMultiRuleScheme()));
        UserProfile wrongOccupationUser = new UserProfile(25, 180000.0, "Maharashtra", "OBC", "Lawyer");

        List<Scheme> result = schemeService.checkEligibility(wrongOccupationUser);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFilterOnlyEligibleSchemesFromMultipleSchemes() {
        Scheme s1 = buildMultiRuleScheme();
        Scheme s2 = new Scheme(2L, "Senior Scheme", "Desc", "Pension", "http://apply", "http://src");
        s2.addRule(new SchemeRule("AGE", "GTE", "60"));

        when(schemeRepository.findAll()).thenReturn(List.of(s1, s2));
        UserProfile youngUser = new UserProfile(25, 180000.0, "Maharashtra", "SC", "Student");

        List<Scheme> result = schemeService.checkEligibility(youngUser);
        assertEquals(1, result.size());
        assertEquals("Test Scheme", result.get(0).getName());
    }

    @Test
    void shouldFindSchemeByIdSuccessfully() {
        Scheme scheme = buildMultiRuleScheme();
        when(schemeRepository.findById(1L)).thenReturn(Optional.of(scheme));

        Scheme result = schemeService.getSchemeById(1L);
        assertNotNull(result);
        assertEquals("Test Scheme", result.getName());
    }

    @Test
    void shouldThrowWhenSchemeByIdNotFound() {
        when(schemeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schemeService.getSchemeById(999L));
    }

    @Test
    void shouldDeleteSchemeWhenFound() {
        Scheme scheme = buildMultiRuleScheme();
        when(schemeRepository.findById(1L)).thenReturn(Optional.of(scheme));

        schemeService.deleteScheme(1L);
        verify(schemeRepository, times(1)).delete(scheme);
    }

    @Test
    void shouldFailFastWhenCreatingSchemeWithInvalidRuleOperator() {
        SchemeRequest request = new SchemeRequest(
                "Bad Scheme", "Desc", "Benefit", null, null,
                List.of(new SchemeRequest.RuleDto("AGE", "INVALID_OPERATOR", "20"))
        );

        assertThrows(IllegalArgumentException.class, () -> schemeService.createScheme(request));
        verify(schemeRepository, never()).save(any());
    }

    @Test
    void shouldCreateSchemeSuccessfullyWithValidRules() {
        SchemeRequest request = new SchemeRequest(
                "Good Scheme", "Desc", "Benefit", "https://apply", "https://source",
                List.of(
                        new SchemeRequest.RuleDto("AGE", "GTE", "18"),
                        new SchemeRequest.RuleDto("OCCUPATION", "EQ", "Farmer")
                )
        );
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Scheme created = schemeService.createScheme(request);
        assertNotNull(created);
        assertEquals("Good Scheme", created.getName());
        assertEquals(2, created.getRules().size());
    }
}

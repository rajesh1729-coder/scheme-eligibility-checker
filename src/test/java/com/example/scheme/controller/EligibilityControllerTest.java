package com.example.scheme.controller;

import com.example.scheme.dto.UserProfile;
import com.example.scheme.model.Scheme;
import com.example.scheme.service.SchemeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Web layer tests verifying input validation and response status for EligibilityController.
@WebMvcTest(EligibilityController.class)
class EligibilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SchemeService schemeService;

    @Test
    void shouldReturnEligibleSchemesWhenRequestIsValid() throws Exception {
        Scheme scheme = new Scheme(1L, "Atal Pension Yojana", "Desc", "Benefit", "http://apply", "http://src");
        when(schemeService.checkEligibility(any(UserProfile.class))).thenReturn(List.of(scheme));

        UserProfile profile = new UserProfile(25, 150000.0, "Maharashtra", "OBC", "Student");

        mockMvc.perform(post("/eligibility/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Atal Pension Yojana"));
    }

    @Test
    void shouldReturn400BadRequestWhenValidationFails() throws Exception {
        // Missing age, negative income, blank state
        String invalidPayload = """
                {
                    "annualIncome": -50000,
                    "state": "",
                    "category": "SC",
                    "occupation": "Student"
                }
                """;

        mockMvc.perform(post("/eligibility/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.age").value("Age is required"))
                .andExpect(jsonPath("$.details.annualIncome").value("Annual income cannot be negative"))
                .andExpect(jsonPath("$.details.state").value("State is required"));
    }
}

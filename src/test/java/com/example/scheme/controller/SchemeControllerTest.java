package com.example.scheme.controller;

import com.example.scheme.dto.SchemeRequest;
import com.example.scheme.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Web layer tests verifying SchemeController endpoints (GET, POST, DELETE) and error scenarios.
@WebMvcTest(SchemeController.class)
class SchemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SchemeService schemeService;

    @Test
    void shouldReturnAllSchemes() throws Exception {
        Scheme s = new Scheme(1L, "Scheme A", "Desc", "Benefit", "http://apply", "http://src");
        when(schemeService.getAllSchemes()).thenReturn(List.of(s));

        mockMvc.perform(get("/schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Scheme A"));
    }

    @Test
    void shouldReturnSchemeByIdWhenFound() throws Exception {
        Scheme s = new Scheme(1L, "Scheme A", "Desc", "Benefit", "http://apply", "http://src");
        when(schemeService.getSchemeById(1L)).thenReturn(s);

        mockMvc.perform(get("/schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Scheme A"));
    }

    @Test
    void shouldReturn404WhenSchemeNotFound() throws Exception {
        when(schemeService.getSchemeById(99L)).thenThrow(new ResourceNotFoundException("Scheme not found with id: 99"));

        mockMvc.perform(get("/schemes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Scheme not found with id: 99"));
    }

    @Test
    void shouldCreateSchemeWhenPayloadIsValid() throws Exception {
        Scheme s = new Scheme(100L, "New Scheme", "Desc", "Benefit", "http://apply", "http://src");
        when(schemeService.createScheme(any(SchemeRequest.class))).thenReturn(s);

        SchemeRequest req = new SchemeRequest(
                "New Scheme", "Desc", "Benefit", "http://apply", "http://src",
                List.of(new SchemeRequest.RuleDto("AGE", "GTE", "18"))
        );

        mockMvc.perform(post("/schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("New Scheme"));
    }

    @Test
    void shouldReturn400WhenCreateSchemeFailsValidation() throws Exception {
        String invalidPayload = """
                {
                    "name": "",
                    "description": "",
                    "benefit": "",
                    "rules": []
                }
                """;

        mockMvc.perform(post("/schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.name").value("Scheme name is required"))
                .andExpect(jsonPath("$.details.rules").value("At least one rule is required"));
    }

    @Test
    void shouldReturn400WhenRuleOperatorIsInvalid() throws Exception {
        when(schemeService.createScheme(any(SchemeRequest.class)))
                .thenThrow(new IllegalArgumentException("Unsupported age operator: INVALID"));

        SchemeRequest req = new SchemeRequest(
                "Bad Scheme", "Desc", "Benefit", null, null,
                List.of(new SchemeRequest.RuleDto("AGE", "INVALID", "18"))
        );

        mockMvc.perform(post("/schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unsupported age operator: INVALID"));
    }

    @Test
    void shouldDeleteSchemeSuccessfully() throws Exception {
        doNothing().when(schemeService).deleteScheme(1L);

        mockMvc.perform(delete("/schemes/1"))
                .andExpect(status().isNoContent());

        verify(schemeService, times(1)).deleteScheme(1L);
    }
}

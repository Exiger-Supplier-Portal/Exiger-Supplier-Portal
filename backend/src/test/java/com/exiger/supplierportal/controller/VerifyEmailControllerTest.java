package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.VerifyEmailRequest;
import com.exiger.supplierportal.service.VerifyEmailService;
import com.exiger.supplierportal.service.UserAccessService;
import com.exiger.supplierportal.service.RegistrationService;
import com.exiger.supplierportal.service.ClientSupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = { "app.frontend.dashboard.url=https://frontend.test/dashboard" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VerifyEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VerifyEmailService verifyEmailService;

    @Autowired
    private ObjectMapper objectMapper;

    // ============================
    //  verifyEmail() POST tests
    // ============================

    @Test
    void verifyEmail_WithValidRequest_ShouldReturnOkAndEmailExistsTrue() throws Exception {
        // Given
        UUID token = UUID.randomUUID();
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setUserEmail("home-depot+wgraham1@terpmail.umd.edu");

        when(verifyEmailService.emailExists("home-depot+wgraham1@terpmail.umd.edu")).thenReturn(true);
        doNothing().when(verifyEmailService).validateRegistrationToken(token);

        // When & Then
        mockMvc.perform(post("/api/verify-email")
                .param("token", token.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token.toString()))
                .andExpect(jsonPath("$.emailExists").value(true));

        verify(verifyEmailService).validateRegistrationToken(token);
        verify(verifyEmailService).emailExists("home-depot+wgraham1@terpmail.umd.edu");
    }

    @Test
    void verifyEmail_WithEmailDoesNotExist_ShouldReturnEmailExistsFalse() throws Exception {
        UUID token = UUID.randomUUID();
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setUserEmail("newuser@test.com");

        when(verifyEmailService.emailExists("newuser@test.com")).thenReturn(false);
        doNothing().when(verifyEmailService).validateRegistrationToken(token);

        mockMvc.perform(post("/api/verify-email")
                .param("token", token.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailExists").value(false));
    }

    @Test
    void verifyEmail_WithInvalidTokenFormat_ShouldReturnBadRequest() throws Exception {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setUserEmail("marriot+wgraham1@terpmail.umd.edu");

        mockMvc.perform(post("/api/verify-email")
                .param("token", "not-a-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyEmail_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        UUID token = UUID.randomUUID();
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setUserEmail(""); // invalid

        mockMvc.perform(post("/api/verify-email")
                .param("token", token.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
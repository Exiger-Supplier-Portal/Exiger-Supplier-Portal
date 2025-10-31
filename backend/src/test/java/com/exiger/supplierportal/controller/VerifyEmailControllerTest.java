package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.VerifyEmailRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.VerifyEmailResponse;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Registration;
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
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private ClientSupplierService clientSupplierService;

    @MockitoBean
    private UserAccessService userAccessService;

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

    // ============================
    //  connectExistingUserToClient() GET tests
    // ============================

    @Test
    void connectExistingUser_WithValidToken_ShouldRedirectToDashboard() throws Exception {
        UUID token = UUID.randomUUID();

        // Mock valid token and registration
        doNothing().when(verifyEmailService).validateRegistrationToken(token);

        Registration registration = new Registration();
        Client client = new Client();
        client.setClientId("client-123");
        registration.setClient(client);
        registration.setSupplierId("supplier-789");
        registration.setToken(token);
        registration.setExpiration(Instant.now().plusSeconds(3600));

        when(registrationService.getRegistrationByToken(token)).thenReturn(registration);

        ClientSupplierResponse clientSupplierResponse = new ClientSupplierResponse();
        clientSupplierResponse.setId(42L);
        when(clientSupplierService.getRelationshipByClientIdSupplierId(eq("client-123"), eq("supplier-789")))
                .thenReturn(clientSupplierResponse);

        doNothing().when(userAccessService).createUserAccess(any(UserAccessRequest.class));

        // When & Then
        mockMvc.perform(get("/api/verify-email/connect")
                .param("token", token.toString())
                .principal(() -> "mcdonalds+wgraham1@terpmail.umd.edu"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://frontend.test/dashboard"));

        verify(verifyEmailService).validateRegistrationToken(token);
        verify(userAccessService).createUserAccess(any(UserAccessRequest.class));
    }

    @Test
    void connectExistingUser_WithInvalidTokenFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/verify-email/connect")
                .param("token", "not-a-uuid")
                .principal(() -> "home-depot+wgraham1@terpmail.umd.edu"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void connectExistingUser_WithExpiredToken_ShouldReturnBadRequest() throws Exception {
        UUID token = UUID.randomUUID();
        doThrow(new IllegalArgumentException("Token expired"))
                .when(verifyEmailService).validateRegistrationToken(token);

        mockMvc.perform(get("/api/verify-email/connect")
                .param("token", token.toString())
                .principal(() -> "home-depot+wgraham1@terpmail.umd.edu"))
                .andExpect(status().isBadRequest());
    }
}

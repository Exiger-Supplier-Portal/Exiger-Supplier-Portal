package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.exception.RegistrationException;
import com.exiger.supplierportal.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerSupplier_WithValidTokenAndRequest_ShouldReturnSuccessResponse() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("Test Company");

        RegistrationResponse response = new RegistrationResponse();
        response.setSuccess(true);
        response.setMessage("Registration successful");
        response.setSupplierId("okta-user-id-123");

        when(registrationService.processRegistration(any(UUID.class), any(RegistrationRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.supplierId").value("okta-user-id-123"));
    }

    @Test
    void registerSupplier_WithInvalidToken_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidToken = "invalid-token";
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("Test Company");

        when(registrationService.processRegistration(any(UUID.class), any(RegistrationRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid UUID string"));

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithExpiredToken_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("Test Company");

        when(registrationService.processRegistration(any(UUID.class), any(RegistrationRequest.class)))
                .thenThrow(new RegistrationException("Invalid or expired registration token"));

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid or expired registration token"));
    }

    @Test
    void registerSupplier_WithMissingToken_ShouldReturnBadRequest() throws Exception {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("Test Company");

        // When & Then
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithMissingEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setSupplierName("Test Company");
        // email is null

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithMissingCompanyName_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        // supplierName is null

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithEmptyEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("");
        request.setSupplierName("Test Company");

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithEmptyCompanyName_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("");

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithInvalidEmailFormat_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("invalid-email");
        request.setSupplierName("Test Company");

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithTooLongCompanyName_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("A".repeat(101)); // Exceeds max length of 100

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithTooShortCompanyName_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("A"); // Below min length of 2

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerSupplier_WithOktaApiFailure_ShouldReturnBadRequest() throws Exception {
        // Given
        String token = UUID.randomUUID().toString();
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@supplier.com");
        request.setSupplierName("Test Company");

        when(registrationService.processRegistration(any(UUID.class), any(RegistrationRequest.class)))
                .thenThrow(new RegistrationException("Failed to create Okta account: API error"));

        // When & Then
        mockMvc.perform(post("/api/register")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Failed to create Okta account: API error"));
    }
}
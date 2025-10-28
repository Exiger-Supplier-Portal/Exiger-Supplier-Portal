package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.InviteRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.InviteResponse;
import com.exiger.supplierportal.service.InviteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = { "api.token=test-token-123" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InviteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InviteService inviteService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void createInvite_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        request.setSupplierEmail("test@supplier.com");
//
//        InviteResponse response = new InviteResponse();
//        response.setRegistrationUrl("https://portal.example.com/register?token=abc-123");
//        response.setExpiresAt(Instant.now().plus(Duration.ofHours(24)));
//
//        when(inviteService.createInvite(any(InviteRequest.class)))
//                .thenReturn(response);
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.registrationUrl").value("https://portal.example.com/register?token=abc-123"))
//                .andExpect(jsonPath("$.expiresAt").exists());
//    }
//
//    @Test
//    void createInvite_WithMissingAuthHeader_ShouldReturnUnauthorized() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        request.setSupplierEmail("test@supplier.com");
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void createInvite_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        request.setSupplierEmail("supplier@example.com");
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer invalid-token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void createInvite_WithMissingClientId_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        // clientId is null
//        request.setSupplierEmail("supplier@example.com");
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createInvite_WithMissingSupplierEmail_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        // supplierEmail is null
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createInvite_WithEmptyClientId_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("");
//        request.setSupplierEmail("supplier@example.com");
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createInvite_WithEmptySupplierEmail_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        request.setSupplierEmail("");
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createInvite_WithInvalidEmailFormat_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        request.setSupplierEmail("invalid-email"); // Invalid email format
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createInvite_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
//        // Given
//        String invalidJson = "{ invalid json }";
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(invalidJson))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createInvite_WithDuplicateInvite_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("CLIENT-001");
//        request.setSupplierEmail("supplier@example.com");
//
//        // Mock service to throw exception for duplicate invite
//        when(inviteService.createInvite(any(InviteRequest.class)))
//                .thenThrow(new IllegalArgumentException("Invite already exists for test@supplier.com"));
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(containsString("Invite already exists")));
//    }
//
//    @Test
//    void createInvite_WithNonExistentClient_ShouldReturnBadRequest() throws Exception {
//        // Given
//        InviteRequest request = new InviteRequest();
//        request.setClientID("NON-EXISTENT-CLIENT");
//        request.setSupplierEmail("supplier@example.com");
//
//        // Mock service to throw exception for non-existent client
//        when(inviteService.createInvite(any(InviteRequest.class)))
//                .thenThrow(new IllegalArgumentException("Client not found with ID: NON-EXISTENT-CLIENT"));
//
//        // When & Then
//        mockMvc.perform(post("/api/invite")
//                .header("Authorization", "Bearer test-token-123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(containsString("Client not found")));
//    }
}
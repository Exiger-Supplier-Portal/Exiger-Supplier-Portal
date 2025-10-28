package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.ClientSupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;
import com.exiger.supplierportal.service.ClientSupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = { "api.token=test-token-123" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientSupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientSupplierService clientSupplierService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRelationship_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("C111");
        request.setSupplierId("S111");
        request.setSupplierName("Test Supplier 1");
        request.setStatus(SupplierStatus.INVITED);

        ClientSupplierResponse response = new ClientSupplierResponse();
        response.setClientId("C111");
        response.setSupplierId("S111");
        response.setSupplierName("Test Supplier 1");
        response.setStatus(SupplierStatus.INVITED);

        when(clientSupplierService.createRelationship(any(ClientSupplierRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/relationship")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value("C111"))
                .andExpect(jsonPath("$.supplierId").value("S111"))
                .andExpect(jsonPath("$.supplierName").value("Test Supplier 1"))
                .andExpect(jsonPath("$.status").value("INVITED"));
    }

    @Test
    void createRelationship_WithMissingAuthHeader_ShouldReturnBadRequest() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.INVITED);

        // When & Then
        mockMvc.perform(post("/api/relationship")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRelationship_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.INVITED);

        // When & Then
        mockMvc.perform(post("/api/relationship")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRelationship_WithDuplicateRelationship_ShouldReturnBadRequest() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.INVITED);

        // Mock service to throw exception for duplicate relationship
        when(clientSupplierService.createRelationship(any(ClientSupplierRequest.class)))
                .thenThrow(new IllegalArgumentException("ClientSupplier already exists between client 1 and supplier 2"));

        // When & Then
        mockMvc.perform(post("/api/relationship")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ClientSupplier already exists")));
    }

    @Test
    void createRelationship_WithSameClientSupplierDifferentStatus_ShouldReturnBadRequest() throws Exception {
        // Given - Try to create relationship with same client/supplier but different
        // status
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.ONBOARDING); // Different status

        // Mock service to throw exception for duplicate relationship
        when(clientSupplierService.createRelationship(any(ClientSupplierRequest.class)))
                .thenThrow(new IllegalArgumentException("ClientSupplier already exists between client 1 and supplier 2"));

        // When & Then
        mockMvc.perform(post("/api/relationship")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ClientSupplier already exists")));
    }

    @Test
    void createRelationship_ClientNotFound_ShouldReturnBadRequest() throws Exception {
        // Given - Try to create relationship with non-existent client
        // status
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("NOT A CLIENT");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.ONBOARDING);

        // Mock service to throw exception
        when(clientSupplierService.createRelationship(any(ClientSupplierRequest.class)))
            .thenThrow(new IllegalArgumentException("Client not found with ID: " + request.getClientId()));

        // When & Then
        mockMvc.perform(post("/api/relationship")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Client not found")));
    }

    // ========== GET ENDPOINT TESTS ==========

    @Test
    void getRelationshipStatus_WithValidApiToken_ShouldReturnRelationshipStatus() throws Exception {
        // Given
        ClientSupplierResponse response = new ClientSupplierResponse();
        response.setClientId("client123");
        response.setSupplierId("supplier456");
        response.setSupplierName("test supplier");
        response.setStatus(SupplierStatus.APPROVED);

        when(clientSupplierService.getRelationshipByClientIdSupplierId("client123", "supplier456"))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/relationship/status")
                .param("clientId", "client123")
                .param("supplierId", "supplier456")
                .header("Authorization", "Bearer test-token-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("client123"))
                .andExpect(jsonPath("$.supplierId").value("supplier456"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getRelationshipStatus_WithMissingAuthHeader_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/relationship/status")
                .param("clientId", "client123")
                .param("supplierId", "supplier456"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRelationshipStatus_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/relationship/status")
                .param("clientId", "client123")
                .param("supplierId", "supplier456")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRelationshipStatus_WithNonExistentRelationship_ShouldReturnBadRequest() throws Exception {
        // Given
        when(clientSupplierService.getRelationshipByClientIdSupplierId("client123", "supplier456"))
                .thenThrow(new RelationshipNotFoundException("client123", "supplier456"));

        // When & Then
        mockMvc.perform(get("/api/relationship/status")
                .param("clientId", "client123")
                .param("supplierId", "supplier456")
                .header("Authorization", "Bearer test-token-123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Relationship not found between client client123 and supplier supplier456"));
    }


    // ========== PUT ENDPOINT TESTS ==========
    @Test
    void updateRelationship_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.ONBOARDING);

        ClientSupplierResponse response = new ClientSupplierResponse();
        response.setClientId("1");
        response.setSupplierId("2");
        response.setSupplierName("test supplier");
        response.setStatus(SupplierStatus.ONBOARDING);

        when(clientSupplierService.updateRelationshipStatus(any(ClientSupplierRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/relationship/status")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value("1"))
                .andExpect(jsonPath("$.supplierId").value("2"))
                .andExpect(jsonPath("$.supplierName").value("test supplier"))
                .andExpect(jsonPath("$.status").value("ONBOARDING"));
    }
    @Test
    void updateRelationship_WithMissingAuthHeader_ShouldReturnUnauthorized() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.ONBOARDING);

        // When & Then
        mockMvc.perform(put("/api/relationship/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
     }

    @Test
    void updateRelationship_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.ONBOARDING);

        // When & Then
        mockMvc.perform(put("/api/relationship/status")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateRelationship_WithNonExistentRelationship_ShouldReturnBadRequest() throws Exception {
        // Given
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("1");
        request.setSupplierId("2");
        request.setSupplierName("test supplier");
        request.setStatus(SupplierStatus.ONBOARDING);

        // Mock service to throw exception for non-existent relationship
        when(clientSupplierService.updateRelationshipStatus(any(ClientSupplierRequest.class)))
                .thenThrow(new RelationshipNotFoundException("1", "2"));

        // When & Then
        mockMvc.perform(put("/api/relationship/status")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Relationship not found between client 1 and supplier 2"));
    }
    // Note: Okta authentication tests would require additional Spring Security test dependencies
    // For now, we're testing the API token endpoints which are the core functionality
}

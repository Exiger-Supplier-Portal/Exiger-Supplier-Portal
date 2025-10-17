package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.service.RelationshipService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = { "api.token=test-token-123" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RelationshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RelationshipService relationshipService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRelationship_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
        // Given
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("1");
        request.setSupplierID("2");
        request.setStatus(SupplierStatus.INVITED);

        RelationshipResponse response = new RelationshipResponse();
        response.setClientID("1");
        response.setSupplierID("2");
        response.setStatus(SupplierStatus.INVITED);

        when(relationshipService.createRelationship(any(RelationshipRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/relationships")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientID").value("1"))
                .andExpect(jsonPath("$.supplierID").value("2"))
                .andExpect(jsonPath("$.status").value("INVITED"));
    }

    @Test
    void createRelationship_WithMissingAuthHeader_ShouldReturnBadRequest() throws Exception {
        // Given
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("1");
        request.setSupplierID("2");
        request.setStatus(SupplierStatus.INVITED);

        // When & Then
        mockMvc.perform(post("/api/relationships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRelationship_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        // Given
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("1");
        request.setSupplierID("2");
        request.setStatus(SupplierStatus.INVITED);

        // When & Then
        mockMvc.perform(post("/api/relationships")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRelationship_WithDuplicateRelationship_ShouldReturnBadRequest() throws Exception {
        // Given
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("1");
        request.setSupplierID("2");
        request.setStatus(SupplierStatus.INVITED);

        // Mock service to throw exception for duplicate relationship
        when(relationshipService.createRelationship(any(RelationshipRequest.class)))
                .thenThrow(new IllegalArgumentException("Relationship already exists between client 1 and supplier 2"));

        // When & Then
        mockMvc.perform(post("/api/relationships")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Relationship already exists")));
    }

    @Test
    void createRelationship_WithSameClientSupplierDifferentStatus_ShouldReturnBadRequest() throws Exception {
        // Given - Try to create relationship with same client/supplier but different
        // status
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("1");
        request.setSupplierID("2");
        request.setStatus(SupplierStatus.ONBOARDING); // Different status

        // Mock service to throw exception for duplicate relationship
        when(relationshipService.createRelationship(any(RelationshipRequest.class)))
                .thenThrow(new IllegalArgumentException("Relationship already exists between client 1 and supplier 2"));

        // When & Then
        mockMvc.perform(post("/api/relationships")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Relationship already exists")));
    }
}

package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.SupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.SupplierResponse;
import com.exiger.supplierportal.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = { "api.token=test-token-123" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupplierService supplierService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSupplier_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
        // Given
        SupplierRequest request = new SupplierRequest();
        request.setSupplierName("Test Supplier");
        request.setSupplierEmail("test@supplier.com");

        SupplierResponse response = new SupplierResponse();
        response.setSupplierID("supplier-uuid-123");
        response.setSupplierName("Test Supplier");
        response.setSupplierEmail("test@supplier.com");

        when(supplierService.createSupplier(any(SupplierRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/supplier")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.supplierID").value("supplier-uuid-123"))
                .andExpect(jsonPath("$.supplierName").value("Test Supplier"))
                .andExpect(jsonPath("$.supplierEmail").value("test@supplier.com"));
    }

    // @Test
    // void createSupplier_WithMissingAuthHeader_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierName("Test Supplier");
    //     request.setSupplierEmail("test@supplier.com");

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest())
    //             .andExpect(jsonPath("$.message").value("Missing Authorization header"));
    // }

    // @Test
    // void createSupplier_WithInvalidAuthHeader_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierName("Test Supplier");
    //     request.setSupplierEmail("test@supplier.com");

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "Bearer invalid-token")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest())
    //             .andExpect(jsonPath("$.message").value("Invalid API token"));
    // }

    // @Test
    // void createSupplier_WithMalformedAuthHeader_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierName("Test Supplier");
    //     request.setSupplierEmail("test@supplier.com");

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "InvalidFormat")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest())
    //             .andExpect(jsonPath("$.message").value("Missing or invalid Authorization header"));
    // }

    // @Test
    // void createSupplier_WithMissingSupplierName_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierEmail("test@supplier.com");
    //     // supplierName is null

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "Bearer test-token-123")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest());
    // }

    // @Test
    // void createSupplier_WithMissingSupplierEmail_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierName("Test Supplier");
    //     // supplierEmail is null

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "Bearer test-token-123")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest());
    // }

    // @Test
    // void createSupplier_WithEmptySupplierName_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierName("");
    //     request.setSupplierEmail("test@supplier.com");

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "Bearer test-token-123")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest());
    // }

    // @Test
    // void createSupplier_WithEmptySupplierEmail_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     SupplierRequest request = new SupplierRequest();
    //     request.setSupplierName("Test Supplier");
    //     request.setSupplierEmail("");

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "Bearer test-token-123")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //             .andExpect(status().isBadRequest());
    // }

    // @Test
    // void createSupplier_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
    //     // Given
    //     String invalidJson = "{ invalid json }";

    //     // When & Then
    //     mockMvc.perform(post("/api/supplier")
    //             .header("Authorization", "Bearer test-token-123")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(invalidJson))
    //             .andExpect(status().isBadRequest());
    // }
}

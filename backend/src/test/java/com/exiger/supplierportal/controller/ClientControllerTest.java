package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.ClientRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientResponse;
import com.exiger.supplierportal.service.ClientService;
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
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClient_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
        // Given
        ClientRequest request = new ClientRequest();
        request.setClientID("C111");
        request.setClientName("Test Client");
        request.setClientEmail("test@client.com");

        ClientResponse response = new ClientResponse();
        response.setClientID("C111");
        response.setClientName("Test Client");
        response.setClientEmail("test@client.com");

        when(clientService.createClient(any(ClientRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/clients")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientID").value("C111"))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.clientEmail").value("test@client.com"));
    }

    @Test
    void createClient_WithMissingAuthHeader_ShouldReturnUnauthorized() throws Exception {
        // Given
        ClientRequest request = new ClientRequest();
        request.setClientID("C111");
        request.setClientName("Test Client");
        request.setClientEmail("test@client.com");

        // When & Then
        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createClient_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        // Given
        ClientRequest request = new ClientRequest();
        request.setClientID("C111");
        request.setClientName("Test Client");
        request.setClientEmail("test@client.com");

        // When & Then
        mockMvc.perform(post("/api/clients")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createClient_WithDuplicateClient_ShouldReturnBadRequest() throws Exception {
        // Given
        ClientRequest request = new ClientRequest();
        request.setClientID("C111");
        request.setClientName("Test Client");
        request.setClientEmail("test@client.com");

        // Mock service to throw exception for duplicate client
        when(clientService.createClient(any(ClientRequest.class)))
                .thenThrow(new IllegalArgumentException("Client already exists with ID: C111"));

        // When & Then
        mockMvc.perform(post("/api/clients")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Client already exists")));
    }

    @Test
    void createClient_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        ClientRequest request = new ClientRequest();
        request.setClientID("C111");
        request.setClientName("Test Client");
        request.setClientEmail("invalid-email"); // Invalid email format

        // When & Then
        mockMvc.perform(post("/api/clients")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createClient_WithBlankFields_ShouldReturnBadRequest() throws Exception {
        // Given
        ClientRequest request = new ClientRequest();
        request.setClientID(""); // Blank ID
        request.setClientName("Test Client");
        request.setClientEmail("test@client.com");

        // When & Then
        mockMvc.perform(post("/api/clients")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
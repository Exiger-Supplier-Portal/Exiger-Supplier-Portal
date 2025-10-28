package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.UserAccountRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccountResponse;
import com.exiger.supplierportal.service.UserAccountService;
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
class UserAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserAccountService userAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_WithValidRequest_ShouldReturnCreatedResponse() throws Exception {
        // Given
        UserAccountRequest request = new UserAccountRequest();
        request.setUserEmail("test@supplier.com");
        request.setFirstName("John");
        request.setLastName("Doe");

        UserAccountResponse response = new UserAccountResponse();
        response.setUserEmail("test@supplier.com");
        response.setFirstName("John");
        response.setLastName("Doe");

        when(userAccountService.createUser(any(UserAccountRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/user")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userEmail").value("test@supplier.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

     @Test
     void createUser_WithMissingAuthHeader_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setUserEmail("test@supplier.com");
         request.setFirstName("John");
         request.setLastName("Doe");

         // When & Then
         mockMvc.perform(post("/api/user")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isUnauthorized())
                 .andExpect(jsonPath("$.message").value("Missing Authorization header"));
     }

     @Test
     void createUser_WithInvalidAuthHeader_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setUserEmail("test@supplier.com");
         request.setFirstName("John");
         request.setLastName("Doe");

         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "Bearer invalid-token")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isUnauthorized())
                 .andExpect(jsonPath("$.message").value("Invalid API token"));
     }

     @Test
     void createUser_WithMalformedAuthHeader_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setUserEmail("test@supplier.com");
         request.setFirstName("John");
         request.setLastName("Doe");

         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "InvalidFormat")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isUnauthorized())
                 .andExpect(jsonPath("$.message").value("Missing or invalid Authorization header"));
     }

     @Test
     void createUser_WithMissingFirstName_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setUserEmail("test@supplier.com");
         request.setLastName("Doe");
         
         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "Bearer test-token-123")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isBadRequest());
     }

    @Test
    void createUser_WithMissingLastName_ShouldReturnBadRequest() throws Exception {
        // Given
        UserAccountRequest request = new UserAccountRequest();
        request.setUserEmail("test@supplier.com");
        request.setFirstName("John");

        // When & Then
        mockMvc.perform(post("/api/user")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

     @Test
     void createUser_WithMissingEmail_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setFirstName("John");
         request.setLastName("Doe");

         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "Bearer test-token-123")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isBadRequest());
     }

     @Test
     void createUser_WithEmptyFirstName_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setFirstName("");
         request.setLastName("Doe");
         request.setUserEmail("test@supplier.com");

         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "Bearer test-token-123")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isBadRequest());
     }

    @Test
    void createUser_WithEmptyLastName_ShouldReturnBadRequest() throws Exception {
        // Given
        UserAccountRequest request = new UserAccountRequest();
        request.setFirstName("John");
        request.setLastName("");
        request.setUserEmail("test@supplier.com");

        // When & Then
        mockMvc.perform(post("/api/user")
                .header("Authorization", "Bearer test-token-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

     @Test
     void createUser_WithEmptyEmail_ShouldReturnBadRequest() throws Exception {
         // Given
         UserAccountRequest request = new UserAccountRequest();
         request.setFirstName("John");
         request.setLastName("Doe");
         request.setUserEmail("");

         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "Bearer test-token-123")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isBadRequest());
     }

     @Test
     void createUser_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
         // Given
         String invalidJson = "{ invalid json }";

         // When & Then
         mockMvc.perform(post("/api/user")
                 .header("Authorization", "Bearer test-token-123")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(invalidJson))
                 .andExpect(status().isBadRequest());
     }
}

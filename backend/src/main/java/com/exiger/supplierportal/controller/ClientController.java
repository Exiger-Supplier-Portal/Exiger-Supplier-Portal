package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.ClientRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing clients.
 * Provides API endpoints for external tools to create clients using API token authentication.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ApiTokenValidator apiTokenValidator;

    /**
     * Creates a new client using ORM persistence.
     * 
     * @param request The client data containing clientID, clientName, and clientEmail
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with created client data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody ClientRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Validate API token
        if (authHeader == null) {
            throw new InvalidApiTokenException("Authorization header is required");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
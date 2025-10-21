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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for managing clients.
 * Provides API endpoints for external tools to create clients using API token authentication.
 */
@Tag(name = "Client Management", description = "Operations for managing clients")
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
    @Operation(
        summary = "Create a new client",
        description = "Creates a new client using API token authentication. Requires valid API token in Authorization header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client created successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing API token"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody ClientRequest request,
            @RequestHeader(value = "Authorization", required = false) 
            @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token") 
            String authHeader) {
        
        // Validate API token
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
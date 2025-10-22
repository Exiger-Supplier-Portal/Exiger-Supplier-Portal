package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

/**
 * REST Controller for managing supplier registration.
 * Provides API endpoints for supplier registration with token validation.
 */
@Tag(name = "Registration Management", description = "Operations for managing supplier registration")
@RestController
@RequestMapping("/api")
@Validated
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Process supplier registration with token validation and Okta account creation.
     * 
     * @param token The registration token from URL parameter
     * @param request The registration form data containing email and supplier name
     * @return ResponseEntity with registration result
     */
    @Operation(
        summary = "Register a new supplier",
        description = "Process supplier registration with token validation and Okta account creation. Requires valid registration token."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid token or request data"),
        @ApiResponse(responseCode = "404", description = "Registration token not found or expired")
    })
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerSupplier(
            @Parameter(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("token") String token,
            @Valid @RequestBody RegistrationRequest request) {
        
        UUID registrationToken = UUID.fromString(token);
        RegistrationResponse response = registrationService.processRegistration(registrationToken, request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.service.SupplierRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing supplier registration.
 * Provides API endpoints for supplier registration with token validation.
 */
@RestController
@RequestMapping("/api")
@Validated
public class RegistrationController {

    @Autowired
    private SupplierRegistrationService supplierRegistrationService;

    /**
     * Process supplier registration with token validation and Okta account creation.
     * 
     * @param token The registration token from URL parameter
     * @param request The registration form data containing email and company name
     * @return ResponseEntity with registration result
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerSupplier(
            @RequestParam("token") String token,
            @Valid @RequestBody RegistrationRequest request) {
        
        UUID registrationToken = UUID.fromString(token);
        RegistrationResponse response = supplierRegistrationService.processRegistration(registrationToken, request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

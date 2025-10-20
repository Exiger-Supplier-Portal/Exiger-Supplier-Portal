package com.exiger.supplierportal.controller;
import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.util.AuthenticationUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.exiger.supplierportal.dto.clientsupplier.request.SupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.SupplierResponse;
import com.exiger.supplierportal.service.SupplierService;

import java.util.List;
/**
 * REST Controller for managing creation of suppliers.
 * Provides API endpoints for external tools to create supplier objects using API token authentication.
 */
@RestController
@RequestMapping("/api/supplier")
@Validated
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ApiTokenValidator apiTokenValidator;

    /**
     * Creates a new supplier using ORM persistence.
     * 
     * @param request The supplier data containing supplierName and supplierEmail
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with created supplier data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @Valid @RequestBody SupplierRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

package com.exiger.supplierportal.controller;
import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.exiger.supplierportal.dto.clientsupplier.request.SupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.SupplierResponse;
import com.exiger.supplierportal.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for managing creation of suppliers.
 * Provides API endpoints for external tools to create supplier objects using API token authentication.
 */
@Tag(name = "Supplier Management", description = "Operations for managing suppliers")
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
    @Operation(
        summary = "Create a new supplier",
        description = "Creates a new supplier using API token authentication. Requires valid API token in Authorization header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Supplier created successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing API token"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @Valid @RequestBody SupplierRequest request,
            @RequestHeader(value = "Authorization", required = false) 
            @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token") 
            String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

package com.exiger.supplierportal.controller;
import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccountRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ApiErrorResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccountResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.exiger.supplierportal.service.UserAccountService;
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
@RequestMapping("/api/user")
@Validated
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final ApiTokenValidator apiTokenValidator;

    /**
     * Creates a new supplier using ORM persistence.
     * 
     * @param request The supplier data containing supplierName and supplierEmail
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with created supplier data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @Operation(
        summary = "Create a new user account",
        description = "Creates a new user account using API token authentication. Requires valid API token in Authorization header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User account created successfully"),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid or missing API token",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<UserAccountResponse> createUser(
            @Valid @RequestBody UserAccountRequest request,
            @RequestHeader(value = "Authorization", required = false) 
            @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token") 
            String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        UserAccountResponse response = userAccountService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

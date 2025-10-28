package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.ClientSupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ApiErrorResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.service.ClientSupplierService;
import com.exiger.supplierportal.util.AuthenticationUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * REST Controller for managing supplier-client relationships.
 * Provides API endpoints for external tools to create relationships using API token authentication.
 */
@Tag(name = "ClientSupplier Management", description = "Operations for managing supplier-client relationships")
@RestController
@RequestMapping("/api/relationship")
@Validated
@RequiredArgsConstructor
public class ClientSupplierController {

    private final ClientSupplierService clientSupplierService;
    private final ApiTokenValidator apiTokenValidator;

    /**
     * Creates a new supplier-client relationship using ORM persistence.
     * 
     * @param request The relationship data containing clientId, supplierId, and status
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with created relationship data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @Operation(
        summary = "Create a new supplier-client relationship",
        description = "Creates a new supplier-client relationship using API token authentication. Requires valid API token in Authorization header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "ClientSupplier created successfully"),
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
    public ResponseEntity<ClientSupplierResponse> createRelationship(
            @Valid @RequestBody ClientSupplierRequest request,
            @RequestHeader(value = "Authorization", required = false) 
            @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token") 
            String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        ClientSupplierResponse response = clientSupplierService.createRelationship(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Gets all ClientSupplier entries for a given user.
     * 
     * @param authentication The Okta authentication object.
     * @return A list of ClientSupplierResponse objects where the supplierId matches.
     */
    @Operation(
        summary = "Get all relationships for authenticated user",
        description = "Gets all relationships for the currently authenticated supplier using OAuth2 authentication. User must be logged in."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relationships retrieved successfully"),
        @ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/my-relationships")
    public ResponseEntity<List<ClientSupplierResponse>> getMyRelationships(Authentication authentication) {
        String userEmail = AuthenticationUtils.getUserEmail(authentication);

        List<ClientSupplierResponse> response = clientSupplierService.getRelationshipsByUserEmail(userEmail);

        return ResponseEntity.ok(response);
    }

    /**
     * Gets the status of a specific relationship between a client and supplier.
     * 
     * @param clientId The ID of the client
     * @param supplierId The ID of the supplier
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with the relationship status
     * @throws InvalidApiTokenException if API token validation fails
     */
    @Operation(
        summary = "Gets the status of a specific relationship between a client and supplier",
        description = "Gets the status of a specific relationship between a client and supplier using API token validation. Requires valid API token in Authorization header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid or missing API token",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "ClientSupplier not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/status")
    public ResponseEntity<ClientSupplierResponse> getRelationshipStatus(
            @Parameter(description = "ID of the client", example = "[client-id]")
            @RequestParam @NotBlank(message = "clientId parameter is required") String clientId,
            @Parameter(description = "ID of the supplier", example = "[supplier-id]")
            @RequestParam @NotBlank(message = "supplierId parameter is required") String supplierId,
            @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token")
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        ClientSupplierResponse response = clientSupplierService.getRelationshipByClientIdSupplierId(clientId, supplierId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the status of an existing supplier-client relationship.
     *
     * @param request The relationship data containing clientId, supplierId, and the new status
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with the updated relationship data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @Operation(
        summary = "Updates the status of an existing Client-Supplier relationship",
        description = "Updates the status of an existing Client-Supplier relationship using API token validation. Requires valid API token in Authorization header."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid or missing API token",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "ClientSupplier not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/status")
    public ResponseEntity<ClientSupplierResponse> updateRelationshipStatus(
        @Valid @RequestBody ClientSupplierRequest request,
        @RequestHeader(value = "Authorization", required = false) 
        @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token") 
        String authHeader) {
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }

        apiTokenValidator.validateApiToken(authHeader);
        ClientSupplierResponse response = clientSupplierService.updateRelationshipStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Gets the status of a specific relationship for the authenticated supplier with a client.
     * 
     * @param clientId The ID of the client
     * @param authentication The Okta authentication object
     * @return ResponseEntity with the relationship status
     */
    @Operation(
        summary = "Get relationship status for authenticated supplier with a client",
        description = "Gets the status of a specific relationship between the authenticated supplier and a client using OAuth2 authentication. UserAccount must be logged in."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
        @ApiResponse(
            responseCode = "401",
            description = "UserAccount not authenticated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/my-status")
    public ResponseEntity<ClientSupplierResponse> getRelationshipStatusBySupplier(
            @Parameter(description = "ID of the client", example = "[client-id]")
            @RequestParam @NotBlank(message = "clientId parameter is required") String clientId,
            Authentication authentication) {
        
        String userEmail = AuthenticationUtils.getUserEmail(authentication);

        ClientSupplierResponse response = clientSupplierService.getRelationshipByClientIdUserEmail(clientId, userEmail);
        return ResponseEntity.ok(response);
    }
}

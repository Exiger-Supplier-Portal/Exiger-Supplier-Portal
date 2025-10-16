package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.service.RelationshipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing supplier-client relationships.
 * Provides API endpoints for external tools to create relationships using API token authentication.
 */
@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private ApiTokenValidator apiTokenValidator;

    /**
     * Creates a new supplier-client relationship using ORM persistence.
     * 
     * @param request The relationship data containing clientID, supplierID, and status
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with created relationship data or error message
     */
    @PostMapping
    public ResponseEntity<?> createRelationship(
            @Valid @RequestBody RelationshipRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // TODO: create a custom exception for invalid API token
        try {
            // Validate API token
            if (authHeader == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Authorization header is required"));
            }
            
            apiTokenValidator.validateApiToken(authHeader);
            
            RelationshipResponse response = relationshipService.createRelationship(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", 401, "message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", 400, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", "An unexpected error occurred"));
        }
    }

    /**
     * Gets all Relationship entries for a given supplier.
     * 
     * @param authentication The Okta authentication object.
     * @return A list of RelationshipResponse objects where the supplierID matches.
     */
    @GetMapping("/clients")
    public List<RelationshipResponse> getClientsBySupplier(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String oktaSub = oidcUser.getAttribute("sub"); // unique okta id

        return relationshipService.getRelationshipsBySupplier(oktaSub);
    }
}

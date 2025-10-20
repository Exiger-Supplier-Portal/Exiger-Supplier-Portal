package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.service.RelationshipService;
import com.exiger.supplierportal.util.AuthenticationUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST Controller for managing supplier-client relationships.
 * Provides API endpoints for external tools to create relationships using API token authentication.
 */
@RestController
@RequestMapping("/api/relationships")
@Validated
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
     * @return ResponseEntity with created relationship data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @PostMapping
    public ResponseEntity<RelationshipResponse> createRelationship(
            @Valid @RequestBody RelationshipRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        RelationshipResponse response = relationshipService.createRelationship(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Gets all Relationship entries for a given supplier.
     * 
     * @param authentication The Okta authentication object.
     * @return A list of RelationshipResponse objects where the supplierID matches.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<RelationshipResponse>> getClientsBySupplier(Authentication authentication) {
        String supplierID = AuthenticationUtils.getSupplierId(authentication);

        List<RelationshipResponse> responseList = relationshipService.getRelationshipsBySupplier(supplierID);
        return ResponseEntity.ok(responseList);
    }

    /**
     * Gets the status of a specific relationship between a client and supplier.
     * 
     * @param clientID The ID of the client
     * @param supplierID The ID of the supplier
     * @param authHeader The Authorization header containing the API token (Bearer format)
     * @return ResponseEntity with the relationship status
     * @throws InvalidApiTokenException if API token validation fails
     */
    @GetMapping("/status")
    public ResponseEntity<RelationshipResponse> getRelationshipStatus(
            @RequestParam @NotBlank(message = "clientID parameter is required") String clientID,
            @RequestParam @NotBlank(message = "supplierID parameter is required") String supplierID,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        RelationshipResponse response = relationshipService.getRelationshipStatus(clientID, supplierID);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets the status of a specific relationship for the authenticated supplier with a client.
     * 
     * @param clientID The ID of the client
     * @param authentication The Okta authentication object
     * @return ResponseEntity with the relationship status
     */
    @GetMapping("/my-status")
    public ResponseEntity<RelationshipResponse> getRelationshipStatusBySupplier(
            @RequestParam @NotBlank(message = "clientID parameter is required") String clientID,
            Authentication authentication) {
        
        String supplierID = AuthenticationUtils.getSupplierId(authentication);

        RelationshipResponse response = relationshipService.getRelationshipStatus(clientID, supplierID);
        return ResponseEntity.ok(response);
    }
}

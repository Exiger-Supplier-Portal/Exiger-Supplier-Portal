package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.config.ApiTokenValidator;
import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.service.RelationshipService;
import com.exiger.supplierportal.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
     * @return ResponseEntity with created relationship data
     * @throws InvalidApiTokenException if API token validation fails
     */
    @PostMapping
    public ResponseEntity<RelationshipResponse> createRelationship(
            @Valid @RequestBody RelationshipRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Validate API token
        if (authHeader == null) {
            throw new InvalidApiTokenException("Authorization header is required");
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
     * Gets all Relationship entries for a given client.
     * 
     * @param authentication The Okta authentication object.
     * @return A list of RelationshipResponse objects where the clientID matches.
     */
    @GetMapping("/suppliers")
    public ResponseEntity<List<RelationshipResponse>> getSuppliersByClient(Authentication authentication) {
        String clientID = AuthenticationUtils.getClientId(authentication);

        List<RelationshipResponse> responseList = relationshipService.getRelationshipsByClient(clientID);
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
    @GetMapping("/status/{clientID}/{supplierID}")
    public ResponseEntity<RelationshipResponse> getRelationshipStatus(
            @PathVariable String clientID,
            @PathVariable String supplierID,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Validate API token
        if (authHeader == null) {
            throw new InvalidApiTokenException("Authorization header is required");
        }
        
        apiTokenValidator.validateApiToken(authHeader);
        
        RelationshipResponse response = relationshipService.getRelationshipStatus(clientID, supplierID);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets the status of a specific relationship for the authenticated client with a supplier.
     * 
     * @param supplierID The ID of the supplier
     * @param authentication The Okta authentication object
     * @return ResponseEntity with the relationship status
     */
    @GetMapping("/my-status/{supplierID}")
    public ResponseEntity<RelationshipResponse> getRelationshipStatusByClient(
            @PathVariable String supplierID,
            Authentication authentication) {
        String clientID = AuthenticationUtils.getClientId(authentication);

        RelationshipResponse response = relationshipService.getRelationshipStatus(clientID, supplierID);
        return ResponseEntity.ok(response);
    }
}

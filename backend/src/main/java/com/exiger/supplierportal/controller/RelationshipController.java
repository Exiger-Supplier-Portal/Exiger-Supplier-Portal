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

/**
 * Controller for managing relationships between clients and suppliers.
 */
@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private ApiTokenValidator apiTokenValidator;

    /**
     * Create a new supplier-client relationship using ORM
     */
    @PostMapping
    public ResponseEntity<RelationshipResponse> createRelationship(
            @Valid @RequestBody RelationshipRequest request,
            @RequestHeader("Authorization") String authHeader) {
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
    public List<RelationshipResponse> getClientsBySupplier(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String oktaSub = oidcUser.getAttribute("sub"); // unique okta id

        return relationshipService.getRelationshipsBySupplier(oktaSub);
    }
}

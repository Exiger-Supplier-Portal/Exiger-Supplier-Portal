package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.service.RelationshipService;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * This controller gets client ids connected to a supplier id.
 */
@RestController
public class RelationshipController {

    private final RelationshipService relationshipService;
    
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    // should return List<Long>, changed for testing purposes
    @GetMapping("/api/clients")
    public List<RelationshipResponse> getClientsBySupplier(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String oktaSub = oidcUser.getAttribute("sub"); // unique okta id

        // TEMP: Hardcoded supplier ID
        Long supplierId = 337987L;

        // Need to map oktaSub to supplierId
        return relationshipService.getRelationshipsBySupplier(supplierId);
    }
}

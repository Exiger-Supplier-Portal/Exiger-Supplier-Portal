package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.service.RelationshipService;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller for getting relationships between clients and suppliers.
 */
@RestController
public class RelationshipController {

    private final RelationshipService relationshipService;
    
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    /**
     * Gets all Relationship entries for a given supplier.
     * 
     * @param authentication The Okta authentication object.
     * @return A list of RelationshipResponse objects where the supplierID matches.
     */
    @GetMapping("/api/clients")
    public List<RelationshipResponse> getClientsBySupplier(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String oktaSub = oidcUser.getAttribute("sub"); // unique okta id

        return relationshipService.getRelationshipsBySupplier(oktaSub);
    }
}

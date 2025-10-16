package com.exiger.supplierportal.controller;

// import com.exiger.supplierportal.service.RelationshipService; add once James/Vincent finishes it
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

// import java.util.List;

/**
 * This controller gets client ids connected to a supplier id.
 */
@RestController
public class RelationshipController {

    // private final RelationshipService relationshipService;
    
    // public RelationshipController(RelationshipService relationshipService) {
    //     this.relationshipService = relationshipService;
    // }

    // should return List<Long>, changed for testing purposes
    @GetMapping("/api/clients")
    public String getClientsBySupplier(Authentication authentication) {
      OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

      // supplier id might be either sub or preferred_username, unsure
      // String oktaSub = oidcUser.getAttribute("sub");
      // String username = oidcUser.getAttribute("preferred_username");

      // return relationshipService.getClientIdsForSupplier(oktaSub);

      System.out.println("User: " + oidcUser);
      System.out.println("Attributes: " + oidcUser.getAttributes());
      
      return "Check console for OIDC user info";
    }
}

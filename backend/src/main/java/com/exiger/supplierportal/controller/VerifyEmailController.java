package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.VerifyEmailRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.VerifyEmailResponse;
import org.springframework.security.core.Authentication;
import com.exiger.supplierportal.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Value;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.service.VerifyEmailService;
import com.exiger.supplierportal.service.UserAccessService;
import com.exiger.supplierportal.service.RegistrationService;
import com.exiger.supplierportal.service.ClientSupplierService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


/**
 * REST Controller for verifying if a user email exists for invite links.
 */
@RestController
@RequestMapping("/api/verify-email")
@Validated
@RequiredArgsConstructor
public class VerifyEmailController {

    private final UserAccessService userAccessService;
    private final VerifyEmailService verifyEmailService;
    private final RegistrationService registrationService;
    private final ClientSupplierService clientSupplierService;

    /** Redirect target for the frontend dashboard after linking a user. */
    @Value("${app.frontend.dashboard.url}")
    private String dashboardUrl;

    /**
     * Verifies if a given user email already exists in the system.
     * 
     * @param token   Registration token included in the invitation link
     * @param request DTO containing the user email to verify
     * @return ResponseEntity containing a flag for whether the email exists
     */
    @PostMapping
    public ResponseEntity<VerifyEmailResponse> verifyEmail(
            @RequestParam("token") 
            @Parameter(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000") 
            String token,
            
            @Valid @RequestBody 
            @Parameter(description = "User email to verify", required = true) 
            VerifyEmailRequest request) {
        
        // Convert the token to UUID and verify it's valid and not expired
        UUID registrationToken = UUID.fromString(token);
        verifyEmailService.validateRegistrationToken(registrationToken);
        
        // Check if a user with this email already exists in the system
        boolean exists = verifyEmailService.emailExists(request.getUserEmail());

        // Build the response object to return to the frontend
        VerifyEmailResponse response = new VerifyEmailResponse();
        response.setToken(token);
        response.setEmailExists(exists);

        // Return HTTP 200 OK with verification results
        return ResponseEntity.ok(response);
    }

    /**
     * Connects an existing authenticated user to a client–supplier relationship.
     * 
     * @param token        Registration token from the invitation link
     * @param authentication The authenticated user object from Spring Security
     * @param response      Used for performing the frontend redirect
     * @throws IOException if redirect fails
     */
    @GetMapping("/connect") 
    public void connectExistingUserToClient(
        @Parameter(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000")
        @RequestParam("token") String token,
        Authentication authentication,
        HttpServletResponse response) throws IOException 
    {
        // Extract email of the authenticated user (retrieved from Okta)
        String userEmail = AuthenticationUtils.getUserEmail(authentication);

        // Convert and validate the token (must exist and be active)
        UUID registrationToken = UUID.fromString(token);
        verifyEmailService.validateRegistrationToken(registrationToken);

        // Retrieve registration information associated with this token
        Registration registration = registrationService.getRegistrationByToken(registrationToken);

        // Extract the client that issued the invitation
        Client client = registration.getClient();

        // Retrieve the Client–Supplier relationship from the database
        ClientSupplierResponse clientSupplierResponse = 
                        clientSupplierService.getRelationshipByClientIdSupplierId(
                            client.getClientId(), registration.getSupplierId());

        // Build a request to create a UserAccess record linking user → client–supplier
        UserAccessRequest userAccessRequest = new UserAccessRequest();
        userAccessRequest.setClientSupplierId(clientSupplierResponse.getId());
        userAccessRequest.setUserEmail(userEmail);

        // Create the new UserAccess record in the system
        userAccessService.createUserAccess(userAccessRequest);

        // Delete row from temp registration table
        registrationService.deleteRegistration(registrationToken);

        // Once linked, redirect the user to the frontend dashboard
        response.sendRedirect(dashboardUrl);
    }
}

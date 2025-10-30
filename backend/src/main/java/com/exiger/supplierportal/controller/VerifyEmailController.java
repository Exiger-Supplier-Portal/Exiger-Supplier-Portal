package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.VerifyEmailRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.VerifyEmailResponse;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccessResponse;
import com.exiger.supplierportal.service.VerifyEmailService;
import com.exiger.supplierportal.service.UserAccessService;
import com.exiger.supplierportal.service.RegistrationService;
import com.exiger.supplierportal.service.ClientSupplierService;
import com.exiger.supplierportal.enums.SupplierStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    /**
     * Checks whether the provided user email already exists.
     * Returns true if email exists, false otherwise.
     * 
     * Frontend will show registration form if email does not exist.
     *
     * @param request DTO containing the user email to verify
     * @return boolean indicating existence of the email
     */
    @PostMapping
    public ResponseEntity<VerifyEmailResponse> verifyEmail(
            @RequestParam("token") 
            @Parameter(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000") 
            String token,
            
            @Valid @RequestBody 
            @Parameter(description = "User email to verify", required = true) 
            VerifyEmailRequest request) {
        
        // Make sure token is valid and not expired
        UUID registrationToken = UUID.fromString(token);
        verifyEmailService.validateRegistrationToken(registrationToken);
        
        // Check if email exists
        boolean exists = verifyEmailService.emailExists(request.getUserEmail());

        // Returns VerifyEmailResponse 
        VerifyEmailResponse response = new VerifyEmailResponse();
        response.setToken(token);
        response.setEmailExists(exists);
        return ResponseEntity.ok(response);
    }

    /**
     * 
     */
    @PostMapping("/connect")
        public ResponseEntity<ConnectUserResponse> connectExistingUserToClient(
            @Parameter(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("oktaToken") String oktaToken,
            @Valid @RequestBody ConnectUserRequest request)
        {

        String regToken = request.getRegistrationToken();

        // TODO OKTA VERIFICAITON

        // Make sure token is valid and not expired
        UUID registrationToken = UUID.fromString(regToken);
        verifyEmailService.validateRegistrationToken(registrationToken);

        // Get Registration from registrationToken
        Registration registration = registrationService.getRegistrationByToken(registrationToken);


        // Extract client from registration
        Client client = registration.getClient();

        // Get the ClientSupplier relationship information
        ClientSupplierResponse clientSupplierResponse = 
                        clientSupplierService.getRelationshipByClientIdSupplierId(client.getClientId(), registration.getSupplierId());



        // Create a UserAccessRequest to populate and use to add a row to UserAccess Table
        UserAccessRequest userAccessRequest = new UserAccessRequest();

        userAccessRequest.setClientSupplierId(clientSupplierResponse.getId());
        userAccessRequest.setUserEmail(registration.getSupplierId());

        userAccessService.createUserAccess(userAccessRequest);



        ConnectUserResponse response = new ConnectUserResponse();
        response.setConnectionSuccess(true);
        return ResponseEntity.ok(response);
    }



}

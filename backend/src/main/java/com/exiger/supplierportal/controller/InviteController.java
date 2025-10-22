package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.InviteRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.InviteResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.service.InviteService;
import com.exiger.supplierportal.config.ApiTokenValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling supplier invitations via POST /api/invite
 * Validates external API token and returns registration link with expiration.
 */
@RestController
@RequestMapping("/api/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @Autowired
    private ApiTokenValidator apiTokenValidator;

    /**
     * Creates a new supplier invite.
     *
     * @param request    The invite request containing clientId and supplierEmail
     * @param authHeader The Authorization header containing API token (Bearer format)
     * @return ResponseEntity with registration URL and expiration
     * @throws InvalidApiTokenException if API token validation fails
     */
    @PostMapping
    public ResponseEntity<InviteResponse> createInvite(
            @Valid @RequestBody InviteRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Validate API token
        if (authHeader == null) {
            throw new InvalidApiTokenException("Missing Authorization header");
        }
        apiTokenValidator.validateApiToken(authHeader);

        InviteResponse response = inviteService.createInvite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

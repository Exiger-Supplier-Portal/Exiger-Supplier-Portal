package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.InviteRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ApiErrorResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.InviteResponse;
import com.exiger.supplierportal.exception.InvalidApiTokenException;
import com.exiger.supplierportal.service.InviteService;
import com.exiger.supplierportal.config.ApiTokenValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling supplier invitations via POST /api/invite
 * Validates external API token and returns registration link with expiration.
 */
@Tag(name = "Invite Management", description = "Operations for inviting suppliers to register for the dashboard")
@RestController
@RequestMapping("/api/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @Autowired
    private ApiTokenValidator apiTokenValidator;

//    /**
//     * Creates a new supplier invite.
//     *
//     * @param request    The invite request containing clientId and supplierEmail
//     * @param authHeader The Authorization header containing API token (Bearer format)
//     * @return ResponseEntity with registration URL and expiration
//     * @throws InvalidApiTokenException if API token validation fails
//     */
//    @Operation(
//        summary = "Create a one-time, temporary invite link for a supplier",
//        description = "Generates unique token and adds row to Registration entity. Requires valid API token in Authorization header."
//    )
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "201", description = "Invite link created successfully"),
//        @ApiResponse(
//            responseCode = "401",
//            description = "Invalid or missing API token",
//            content = @Content(mediaType = "application/json",
//                schema = @Schema(implementation = ApiErrorResponse.class))),
//        @ApiResponse(
//            responseCode = "400",
//            description = "Invalid request data",
//            content = @Content(mediaType = "application/json",
//                schema = @Schema(implementation = ApiErrorResponse.class)))
//    })
//    @PostMapping
//    public ResponseEntity<InviteResponse> createInvite(
//            @Valid @RequestBody InviteRequest request,
//            @Parameter(description = "Bearer token for API authentication", example = "Bearer your-api-token")
//            @RequestHeader(value = "Authorization", required = false) String authHeader) {
//
//        // Validate API token
//        if (authHeader == null) {
//            throw new InvalidApiTokenException("Missing Authorization header");
//        }
//        apiTokenValidator.validateApiToken(authHeader);
//
//        InviteResponse response = inviteService.createInvite(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
}

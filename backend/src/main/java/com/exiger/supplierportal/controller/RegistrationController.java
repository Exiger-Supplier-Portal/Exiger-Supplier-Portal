package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ApiErrorResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.service.RegistrationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

/**
 * REST Controller for managing supplier registration.
 * Provides API endpoint to complete registration using a one-time token.
 */
@Tag(name = "Registration Management", description = "Operations for managing supplier registration")
@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Process supplier registration with token validation and Okta account creation.
     *
     * @param token Registration token from the URL
     * @param request User email, first and last name collected from the form
     * @return RegistrationResponse with success, message, and user email
     */
    @Operation(
        summary = "Register a supplier",
        description = "Validates a registration token, creates the user account and access, provisions Okta, and finalizes registration."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration processed successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid token or request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerSupplier(
            @Parameter(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("token") String token,
            @Valid @RequestBody RegistrationRequest request) {

        UUID registrationToken = UUID.fromString(token);
        RegistrationResponse response = registrationService.processRegistration(registrationToken, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

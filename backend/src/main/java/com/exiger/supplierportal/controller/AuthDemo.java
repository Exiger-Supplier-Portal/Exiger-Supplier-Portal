package com.exiger.supplierportal.controller;

import com.exiger.supplierportal.dto.clientsupplier.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * This is an example controller to show authentication requirements of endpoints
 * /api/whoami requires users to be authenticated, while /api/hello allows for anonymous access
 * See config/SecurityConfig.java
 */
@Tag(name = "Testing Management", description = "Operations for Testing Authentication")
@RestController
public class AuthDemo {
    @Operation(
        summary = "Anonymous access endpoint",
        description = "Returns the string 'anonymous access'. This endpoint allows anonymous access for testing purposes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anonymous access message returned successfully")
    })
    @GetMapping("/api/hello")
    public String anon() {
        return "Anonymous access";
    }

    @Operation(
        summary = "Get authenticated user details",
        description = "Returns authentication details for the currently logged-in user. Requires OAuth2 authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication details returned successfully"),
        @ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/api/whoami")
    public String whoami(Authentication authentication) {
        return authentication.getDetails().toString();
    }
}




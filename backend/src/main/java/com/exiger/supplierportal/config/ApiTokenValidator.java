package com.exiger.supplierportal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Validates API tokens for external tool authentication.
 * Provides secure token validation for API endpoints.
 */
@Component
public class ApiTokenValidator {

    @Value("${api.token:default-token}")
    private String validApiToken;

    /**
     * Validates API token from Authorization header.
     * 
     * @param authHeader The Authorization header containing the Bearer token
     * @throws SecurityException if token is missing, invalid format, or doesn't match
     */
    public void validateApiToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        
        if (!validApiToken.equals(token)) {
            throw new SecurityException("Invalid API token");
        }
    }
}

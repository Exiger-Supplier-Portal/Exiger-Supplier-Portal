package com.exiger.supplierportal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiTokenValidator {

    @Value("${api.token:default-token}")
    private String validApiToken;

    /**
     * Validate API token from Authorization header
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

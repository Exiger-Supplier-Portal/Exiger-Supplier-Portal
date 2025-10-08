package com.exiger.supplierportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication controller providing endpoints for user authentication status,
 * login redirection, and logout functionality.
 * <p>
 * This controller handles the OAuth2 authentication flow with Okta and provides
 * JSON responses for frontend consumption.
 */
@RestController
public class Auth {

    /**
     * Returns the current authentication status of the user.
     * 
     * @param authentication Spring Security authentication object containing user details
     * @return JSON response with authentication status, session details, and supplier ID
     */
    @GetMapping("/api/auth/status")
    public Map<String, Object> getAuthStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {

            response.put("authenticated", true);
            response.put("sessionDetails", authentication.getDetails().toString());

            // TODO: Get from session or user data
            // This is a placeholder for the supplier ID
            response.put("supplierId", "SUPPLIER_123");

        } else {
            response.put("authenticated", false);
        }

        return response;
    }

    /**
     * Redirects the user to the Okta OAuth2 login page.
     * <p>
     * This endpoint initiates the OAuth2 authorization code flow with PKCE.
     * Spring Security handles the complete authentication flow including
     * token exchange and session creation.
     * 
     * @param response HTTP response object for redirection
     * @throws IOException if redirection fails
     */
    @GetMapping("/api/auth/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/okta");
    }

    /**
     * Logs out the current user and redirects to Spring Security's logout endpoint.
     * <p>
     * This endpoint provides a consistent logout URL for frontend applications.
     * It redirects to Spring Security's built-in logout which handles both
     * local session invalidation and Okta logout.
     * 
     * @param response HTTP response object for redirection
     * @return JSON response with logout information before redirect
     * @throws IOException if redirection fails
     */
    @GetMapping("/api/auth/logout")
    public Map<String, Object> logout(HttpServletResponse response) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Redirecting to logout...");
        result.put("logoutUrl", "/logout");

        response.sendRedirect("/logout");

        return result;
    }
}

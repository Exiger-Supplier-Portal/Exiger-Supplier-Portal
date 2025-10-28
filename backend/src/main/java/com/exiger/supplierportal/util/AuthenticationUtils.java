package com.exiger.supplierportal.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * Utility class for extracting information from Spring Security Authentication objects.
 * Provides common methods for working with Okta authentication.
 */
public class AuthenticationUtils {

    /**
     * Extracts the sub ID from Okta authentication.
     * 
     * @param authentication The Okta authentication object
     * @return The sub ID attribute
     */
    public static String getSubId(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        return oidcUser.getAttribute("sub"); // unique okta id
    }

    /**
     * Extracts the user's email from Okta authentication.
     *
     * @param authentication The Okta authentication object
     * @return The user's email address
     */
    public static String getUserEmail(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        return oidcUser.getEmail();
    }
}

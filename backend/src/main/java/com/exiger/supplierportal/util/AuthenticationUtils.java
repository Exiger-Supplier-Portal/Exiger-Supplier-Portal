package com.exiger.supplierportal.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * Utility class for extracting information from Spring Security Authentication objects.
 * Provides common methods for working with Okta authentication.
 */
public class AuthenticationUtils {

    /**
     * Extracts the client ID from Okta authentication.
     * 
     * @param authentication The Okta authentication object
     * @return The client ID (Okta sub attribute)
     */
    public static String getClientId(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        return oidcUser.getAttribute("sub"); // unique okta id
    }

    /**
     * Extracts the supplier ID from Okta authentication.
     * Note: In this system, supplier ID is the same as client ID (Okta sub attribute).
     * 
     * @param authentication The Okta authentication object
     * @return The supplier ID (Okta sub attribute)
     */
    public static String getSupplierId(Authentication authentication) {
        return getClientId(authentication); // Same as client ID in this system
    }
}

package com.exiger.supplierportal.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is an example controller to show authentication requirements of endpoints
 * /api/whoami requires users to be authenticated, while /api/hello allows for anonymous access
 * See config/SecurityConfig.java
 */
@RestController
public class AuthDemo {
    @GetMapping("/api/hello")
    public String anon() {
        return "Anonymous access";
    }

    @GetMapping("/api/whoami")
    public String whoami(Authentication authentication) {
        return authentication.getDetails().toString();
    }
}




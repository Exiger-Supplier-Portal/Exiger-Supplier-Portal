package com.exiger.supplierportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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




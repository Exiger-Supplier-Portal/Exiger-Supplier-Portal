package com.exiger.supplierportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

/**
 * This class configures endpoint access permissions.
 * By default, any anonymous request to any endpoint returns a 401 Unauthorized error.
 * In this example, we require users to be authenticated when accessing /api/whoami using .authenticated().
 * If the user tries to access it without being authenticated, the server returns 401 Unauthorized error
 * We allow anyone to access all other endpoints using .permitAll()
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
            (req) -> req.requestMatchers("/api/whoami").authenticated()
                    .requestMatchers("/**").permitAll() // any
            )
            .oauth2ResourceServer((srv) -> srv.jwt(Customizer.withDefaults()))
            .build();
    }
}

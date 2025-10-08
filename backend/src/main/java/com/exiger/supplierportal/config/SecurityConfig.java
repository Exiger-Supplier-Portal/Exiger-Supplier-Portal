package com.exiger.supplierportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

/**
 * This class configures endpoint access permissions.
 * By default, any anonymous request to any endpoint returns a 401 Unauthorized error.
 * We allow one endpoint, /api/hello, to be accessible to anonymous users
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(req -> req
                    .requestMatchers("/api/hello").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}

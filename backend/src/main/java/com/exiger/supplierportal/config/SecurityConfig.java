package com.exiger.supplierportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * This class configures endpoint access permissions.
 * By default, any anonymous request to any endpoint returns a 401 Unauthorized error.
 * We allow one endpoint, /api/hello, to be accessible to anonymous users
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository repo) throws Exception {
        http
            .authorizeHttpRequests(req -> req
                .requestMatchers("/", "/api/hello", "/api/auth/login", "/api/auth/status").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults())
            .logout((logout) -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler(repo))
            );

        return http.build();
    }

    /**
     * Enables RP-initiated logout, which allows us to request that Okta log out the user
     * @param repo Spring automatically creates this according to application.properties and injects it
     * @return LogoutSuccessHandler
     */
    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository repo) {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
            new OidcClientInitiatedLogoutSuccessHandler(repo);

        successHandler.setPostLogoutRedirectUri("{baseUrl}/");
        return successHandler;
    }
}

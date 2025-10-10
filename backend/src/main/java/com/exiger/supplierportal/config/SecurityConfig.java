package com.exiger.supplierportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(req -> req
                .requestMatchers("/", "/api/hello").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults())
            .logout((logout) -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler(repo))
            );

        return http.build();
    }

    /**
     * CORS configuration to allow frontend requests with credentials.
     * This is critical for JSESSIONID cookie to work properly with the frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Critical for JSESSIONID cookie
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

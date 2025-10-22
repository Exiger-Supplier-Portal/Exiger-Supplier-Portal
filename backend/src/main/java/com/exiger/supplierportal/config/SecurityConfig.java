package com.exiger.supplierportal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * This class configures endpoint access permissions.
 * By default, any anonymous request to any endpoint returns a 401 Unauthorized error.
 * We allow one endpoint, /api/hello, to be accessible to anonymous users
 */
@Configuration
public class SecurityConfig {
    
    @Value("${cors.allowed.origins}")
    private String corsAllowedOrigins;

    @Value("${app.frontend.dashboard.url}")
    private String dashboardUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository repo) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(req -> req
                .requestMatchers("/", "/api/hello", "/api/relationships", "/api/relationships/status", "/api/clients", "/api/supplier", "/api/register").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                    .successHandler(customAuthenticationSuccessHandler()))
            .logout((logout) -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler(repo))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(AbstractHttpConfigurer::disable); // Disable CSRF for API endpoints

        return http.build();
    }

    /**
     * CORS configuration to allow frontend requests with credentials.
     * This is critical for JSESSIONID cookie to work properly with the frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Parse comma-separated origins from environment variable
        List<String> allowedOrigins = Arrays.asList(corsAllowedOrigins.split(","));
        configuration.setAllowedOriginPatterns(allowedOrigins);
        
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

        successHandler.setPostLogoutRedirectUri(frontendUrl);
        return successHandler;
    }

    /**
     * Redirects user back to frontend dashboard after logging in successfully through Okta
     * @return AuthenticationSuccessHandler - redirecting user to /dashboard
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect(dashboardUrl); // Redirect to frontend dashboard
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

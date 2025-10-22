package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.SupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.exception.RegistrationException;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.repository.RegistrationRepository;
import com.exiger.supplierportal.repository.SupplierRepository;
import com.exiger.supplierportal.enums.SupplierStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing supplier registrations
 * Business logic for adding registrations and transferring from registration to full account and relationship
 */
@Service
@Transactional
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.provider.okta.issuer-uri}")
    private String oktaDomain;

    @Value("${okta.api.token}")
    private String oktaApiToken;

    /**
     * Process supplier registration with token validation and Okta account creation.
     * 
     * @param token The registration token from URL parameter
     * @param request The registration form data
     * @return RegistrationResponse with success status and supplier ID
     * @throws RegistrationException if token is invalid or expired
     */
    public RegistrationResponse processRegistration(UUID token, RegistrationRequest request) {
        // 1. Verify token is valid and not expired
        Optional<Registration> registrationOpt = registrationRepository
                .findByTokenAndExpirationAfter(token, LocalDateTime.now());
        
        if (registrationOpt.isEmpty()) {
            throw new RegistrationException("Invalid or expired registration token");
        }

        Registration registration = registrationOpt.get();

        // 2. Check if supplier already exists with this email
        Optional<Supplier> existingSupplier = supplierRepository.findBySupplierEmail(request.getEmail());
        
        String supplierId;
        if (existingSupplier.isPresent()) {
            // Supplier already exists, use existing ID
            supplierId = existingSupplier.get().getSupplierID();
        } else {
            // Create Okta account and get the user ID
            supplierId = createOktaAccount(request.getEmail(), request.getSupplierName());
            
            // Create supplier record using SupplierService with Okta ID
            SupplierRequest supplierRequest = new SupplierRequest();
            supplierRequest.setSupplierID(supplierId);
            supplierRequest.setSupplierName(request.getSupplierName());
            supplierRequest.setSupplierEmail(request.getEmail());
            
            supplierService.createSupplier(supplierRequest);
        }

        // Create relationship between client and supplier
        RelationshipRequest relationshipRequest = new RelationshipRequest();
        relationshipRequest.setClientID(registration.getClient().getClientID());
        relationshipRequest.setSupplierID(supplierId);
        relationshipRequest.setStatus(SupplierStatus.ONBOARDING);
        
        relationshipService.createRelationship(relationshipRequest);

        // Clean up: Delete the registration record since it's no longer needed
        registrationRepository.deleteByToken(token);
        
        RegistrationResponse response = new RegistrationResponse();
        response.setSuccess(true);
        response.setMessage("Registration successful");
        response.setSupplierId(supplierId);
        return response;
    }

    /**
     * Create Okta user account and return the Okta user ID.
     * 
     * @param email The user's email address
     * @param companyName The company name (used for first/last name)
     * @return Okta user ID
     * @throws RegistrationException if Okta account creation fails
     */
    private String createOktaAccount(String email, String companyName) {
        try {
            // Extract Okta domain from issuer URI (remove /oauth2/default)
            String oktaOrgUrl = oktaDomain.replace("/oauth2/default", "");
            
            // Prepare Okta user creation request
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("email", email);
            userProfile.put("login", email);
            userProfile.put("firstName", companyName); // Using company name as first name
            userProfile.put("lastName", "Supplier"); // Generic last name
            
            Map<String, Object> oktaUser = new HashMap<>();
            oktaUser.put("profile", userProfile);
            
            // No credentials needed - user will set password via activation email
            
            // Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "SSWS " + oktaApiToken);
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(oktaUser, headers);
            
            // Make API call to Okta
            String oktaApiUrl = oktaOrgUrl + "/api/v1/users?activate=false";
            
            ResponseEntity<Map> response = restTemplate.exchange(
                oktaApiUrl, 
                HttpMethod.POST, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                if (responseBody != null) {
                    String oktaUserId = (String) responseBody.get("id");
                    
                    if (oktaUserId != null) {
                        // Automatically trigger activation email after user creation
                        String activateUrl = oktaOrgUrl + "/api/v1/users/" + oktaUserId + "/lifecycle/activate?sendEmail=true";
                        HttpEntity<Void> activateRequest = new HttpEntity<>(headers);
                        restTemplate.postForObject(activateUrl, activateRequest, Map.class);
                        
                        return oktaUserId;
                    }
                }
            }
            
            throw new RegistrationException("Failed to create Okta account: Invalid response");
            
        } catch (Exception e) {
            throw new RegistrationException("Failed to create Okta account: " + e.getMessage(), e);
        }
    }

}

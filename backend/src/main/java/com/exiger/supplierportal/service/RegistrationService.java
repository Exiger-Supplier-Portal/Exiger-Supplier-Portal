package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccountRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.exception.RegistrationException;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;
import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.repository.ClientSupplierRepository;
import com.exiger.supplierportal.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
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
    private ClientSupplierRepository clientSupplierRepository;

    @Autowired
    private UserAccountService userAccountService;

    // Removed unused ClientSupplierService dependency

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.provider.okta.issuer-uri}")
    private String oktaDomain;

    @Value("${okta.api.token}")
    private String oktaApiToken;

    /**
     * Process a supplier registration using a one-time token.
     * Steps:
     * 1) Validate token and load the pending Registration
     * 2) Create UserAccount from invite email and provided first/last name
     * 3) Resolve ClientSupplier (clientId + supplierId) and create UserAccess linking the user
     * 4) Create Okta user and send activation email
     * 5) Delete the Registration row to prevent reuse
     *
     * @param token one-time registration token
     * @param request first and last name captured from the form
     * @return response containing success, message and Okta user ID
     * @throws RegistrationException if token is invalid/expired or any step fails
     */
    public RegistrationResponse processRegistration(UUID token, RegistrationRequest request) {
        // 1) Validate token and load registration
        Optional<Registration> registrationOpt = registrationRepository.findByTokenAndExpirationAfter(token, Instant.now());
        if (registrationOpt.isEmpty()) {
            throw new RegistrationException("Invalid or expired registration token");
        }
        Registration registration = registrationOpt.get();

        String userEmail = registration.getInviteEmail();
        String clientId = registration.getClient().getClientId();
        String supplierId = registration.getSupplierId();

        // 2) Create UserAccount
        UserAccountRequest userReq = new UserAccountRequest();
        userReq.setUserEmail(userEmail);
        userReq.setFirstName(request.getFirstName());
        userReq.setLastName(request.getLastName());
        userAccountService.createUser(userReq);

        // 3) Resolve ClientSupplier and create UserAccess
        ClientSupplier clientSupplier = clientSupplierRepository
            .findByClient_ClientIdAndSupplierId(clientId, supplierId)
            .orElseThrow(() -> new RelationshipNotFoundException(clientId, supplierId));

        UserAccessRequest accessReq = new UserAccessRequest();
        accessReq.setClientSupplierId(clientSupplier.getId());
        accessReq.setUserEmail(userEmail);
        userAccessService.createUserAccess(accessReq);

        // 4) Create Okta account (triggers activation email)
        String oktaUserId = createOktaAccount(userEmail, request.getFirstName());

        // 5) Cleanup registration row
        registrationRepository.deleteByToken(token);

        RegistrationResponse response = new RegistrationResponse();
        response.setSuccess(true);
        response.setMessage("Registration successful");
        response.setSupplierId(oktaUserId);
        return response;
    }
    

    /**
     * Create Okta user account and return the Okta user ID.
     * 
     * @param email The user's email address
     * @param supplierName The supplier name (used for first/last name)
     * @return Okta user ID
     * @throws RegistrationException if Okta account creation fails
     */
    /**
     * Create an Okta user for the given email and name, then trigger activation email.
     *
     * @param email user email to provision in Okta
     * @param supplierName used as first name for the Okta profile
     * @return Okta user ID
     */
    private String createOktaAccount(String email, String supplierName) {
        try {
            // Extract Okta domain from issuer URI (remove /oauth2/default)
            String oktaOrgUrl = oktaDomain.replace("/oauth2/default", "");
            
            // Prepare Okta user creation request
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("email", email);
            userProfile.put("login", email);
            userProfile.put("firstName", supplierName); // Using supplier name as first name
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
            
            ResponseEntity<java.util.Map<String, Object>> response = restTemplate.exchange(
                oktaApiUrl, 
                HttpMethod.POST, 
                request, 
                (Class<java.util.Map<String, Object>>)(Class<?>)java.util.Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                java.util.Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String oktaUserId = (String) responseBody.get("id");
                    
                    if (oktaUserId != null) {
                        // Automatically trigger activation email after user creation
                        String activateUrl = oktaOrgUrl + "/api/v1/users/" + oktaUserId + "/lifecycle/activate?sendEmail=true";
                        HttpEntity<Void> activateRequest = new HttpEntity<>(headers);
                        restTemplate.postForObject(activateUrl, activateRequest, java.util.Map.class);
                        
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

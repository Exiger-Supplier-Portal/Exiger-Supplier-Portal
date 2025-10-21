package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.exception.RegistrationException;
import com.exiger.supplierportal.model.SupplierRegistration;
import com.exiger.supplierportal.repository.SupplierRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing supplier registrations
 * Business logic for adding registrations and transferring from registration to full account and relationship
 */
@Service
@Transactional
public class SupplierRegistrationService {

    @Autowired
    private SupplierRegistrationRepository supplierRegistrationRepository;

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
        Optional<SupplierRegistration> registrationOpt = supplierRegistrationRepository
                .findByTokenAndExpirationAfter(token, LocalDateTime.now());
        
        if (registrationOpt.isEmpty()) {
            throw new RegistrationException("Invalid or expired registration token");
        }

        SupplierRegistration registration = registrationOpt.get();
        
        // TODO: Continue with next steps
        RegistrationResponse response = new RegistrationResponse();
        response.setSuccess(false);
        response.setMessage("Token validation complete - next steps pending");
        return response;
    }
}

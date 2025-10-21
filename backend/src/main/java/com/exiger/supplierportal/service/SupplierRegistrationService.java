package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.exception.RegistrationException;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.model.SupplierRegistration;
import com.exiger.supplierportal.repository.SupplierRegistrationRepository;
import com.exiger.supplierportal.repository.SupplierRepository;
import com.exiger.supplierportal.enums.SupplierStatus;
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

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private RelationshipService relationshipService;

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

        // 2. Check if supplier already exists with this email
        Optional<Supplier> existingSupplier = supplierRepository.findBySupplierEmail(request.getEmail());
        
        String supplierId;
        if (existingSupplier.isPresent()) {
            // Supplier already exists, use existing ID
            supplierId = existingSupplier.get().getSupplierID();
        } else {
            // TODO: Create Okta account
            throw new RegistrationException("Okta account creation not implemented yet");
        }

        // Create relationship between client and supplier
        RelationshipRequest relationshipRequest = new RelationshipRequest();
        relationshipRequest.setClientID(registration.getClient().getClientID());
        relationshipRequest.setSupplierID(supplierId);
        relationshipRequest.setStatus(SupplierStatus.ONBOARDING);
        
        relationshipService.createRelationship(relationshipRequest);

        // TODO: Delete registration record
        
        RegistrationResponse response = new RegistrationResponse();
        response.setSuccess(true);
        response.setMessage("Registration successful");
        response.setSupplierId(supplierId);
        return response;
    }

}

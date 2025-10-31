package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.InviteRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.InviteResponse;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.repository.RegistrationRepository;
import com.exiger.supplierportal.repository.ClientSupplierRepository;
import com.exiger.supplierportal.repository.ClientRepository;
import com.exiger.supplierportal.enums.SupplierStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class InviteService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientSupplierRepository clientSupplierRepository;

    @Value("${app.frontend.url}")
    private String frontendBaseUrl;

    /**
     * Creates a Registration, which generates a one-time temporary registration link for a supplier
     *
     * @param request InviteRequest containing clientID, supplierID, and supplierEmail
     * @return InviteResponse containing registrationUrl and expiration
     * @throws IllegalArgumentException if client not found
     */
    public InviteResponse createInvite(InviteRequest request) {
       // Validate client exists
       Client client = clientRepository.findById(request.getClientId())
               .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Check whether relationship exists between given supplier and client
        Boolean relationshipExists = clientSupplierRepository.existsByClient_ClientIdAndSupplierId(request.getClientId(), request.getSupplierId());
        // if it doesn't exist, then create a new relationship row
        // has original status of enum: INVITED 
        if (!relationshipExists) {
            SupplierStatus initial = SupplierStatus.INVITED;
            ClientSupplier newClientSupplier = new ClientSupplier();
            newClientSupplier.setClient(client);
            newClientSupplier.setSupplierId(request.getSupplierId());
            newClientSupplier.setStatus(initial);
            newClientSupplier.setSupplierName(request.getSupplierId());
            clientSupplierRepository.save(newClientSupplier);
        }
        // Generate registration token
        UUID token = UUID.randomUUID();
        Instant expiration = Instant.now().plus(Duration.ofHours(24)); // 24 hours expiry

        Registration registration = new Registration();
        registration.setClient(client);
        registration.setSupplierId(request.getSupplierId());
        registration.setInviteEmail(request.getSupplierEmail());
        registration.setToken(token);
        registration.setExpiration(expiration);

        registrationRepository.save(registration);

        // Build registration URL
        String registrationUrl = frontendBaseUrl + "/register?token=" + token;

        InviteResponse response = new InviteResponse();
        response.setRegistrationUrl(registrationUrl);
        response.setExpiresAt(expiration);

        return response;
   }
}

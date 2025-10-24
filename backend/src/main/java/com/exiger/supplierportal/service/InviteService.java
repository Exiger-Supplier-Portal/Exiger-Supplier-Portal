package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.InviteRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.InviteResponse;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.repository.RegistrationRepository;
import com.exiger.supplierportal.repository.ClientRepository;
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

    @Value("${app.frontend.url}")
    private String frontendBaseUrl;

    /**
     * Creates a Registration, which generates a one-time temporary registration link for a supplier
     *
     * @param request InviteRequest containing clientID and supplierEmail
     * @return InviteResponse containing registrationUrl and expiration
     * @throws IllegalArgumentException if client not found
     */
    public InviteResponse createInvite(InviteRequest request) {
        // Validate client exists
        Client client = clientRepository.findById(request.getClientID())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Generate registration token
        UUID token = UUID.randomUUID();
        Instant expiration = Instant.now().plus(Duration.ofHours(24)); // 24 hours expiry

        Registration registration = new Registration();
        registration.setClient(client);
        registration.setSupplierEmail(request.getSupplierEmail());
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

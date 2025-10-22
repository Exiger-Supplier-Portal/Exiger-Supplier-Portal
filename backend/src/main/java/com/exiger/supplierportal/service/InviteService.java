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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class InviteService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Value("${app.frontend.url}")
    private String frontendBaseUrl;

    @Transactional
    public InviteResponse createInvite(InviteRequest request) {
        // Validate client exists
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalStateException("Client not found"));

        // Generate registration token
        UUID token = UUID.randomUUID();
        LocalDateTime expiration = LocalDateTime.now().plusHours(24); // 24 hours expiry

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
        response.setExpiresAt(expiration.toInstant(ZoneOffset.UTC));

        return response;
    }
}

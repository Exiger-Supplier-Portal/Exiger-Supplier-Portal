package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.ClientRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientResponse;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service for managing Client entities.
 * Provides business logic for retrieving and creating client information.
 */
@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Creates a new client using ORM persistence.
     * 
     * @param request The client request containing clientID, clientName, and clientEmail
     * @return ClientResponse containing the created client data
     * @throws IllegalArgumentException if a client already exists with the given ID
     */
    public ClientResponse createClient(ClientRequest request) {
        // Check if client already exists
        if (clientRepository.existsById(request.getClientID())) {
            throw new IllegalArgumentException(
                "Client already exists with ID: " + request.getClientID());
        }

        // Create new client entity
        Client client = new Client();
        client.setClientID(request.getClientID());
        client.setClientName(request.getClientName());
        client.setClientEmail(request.getClientEmail());

        // Save to database using JPA/Hibernate ORM
        Client savedClient = clientRepository.save(client);
        
        // Convert to response DTO
        return convertToResponse(savedClient);
    }

    /**
     * Retrieves all client IDs from the database.
     *
     * @return A list of client IDs.
     */
    public List<String> getAllClientIDs() {
        return clientRepository.findAllClientIDs();
    }

    /**
     * Convert Client entity to ClientResponse DTO
     */
    private ClientResponse convertToResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setClientID(client.getClientID());
        response.setClientName(client.getClientName());
        response.setClientEmail(client.getClientEmail());
        return response;
    }
}

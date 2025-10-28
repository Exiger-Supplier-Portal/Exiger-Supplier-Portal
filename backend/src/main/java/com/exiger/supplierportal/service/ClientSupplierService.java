package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.ClientSupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.repository.ClientRepository;
import com.exiger.supplierportal.repository.ClientSupplierRepository;
import com.exiger.supplierportal.repository.UserAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;


/**
 * Service class for managing supplier-client relationships.
 * Provides business logic for creating relationships using JPA/Hibernate ORM.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ClientSupplierService {
    private final ClientSupplierRepository clientSupplierRepository;
    private final ClientRepository clientRepository;
    private final UserAccessRepository userAccessRepository;

    /**
     * Creates a new supplier-client relationship using ORM persistence.
     * 
     * @param request The relationship request containing clientID, supplierID, status, and supplierName
     * @return ClientSupplierResponse containing the created relationship data
     * @throws IllegalArgumentException if a relationship already exists between the client and supplier
     */
    public ClientSupplierResponse createRelationship(ClientSupplierRequest request) {
        // Check if clientSupplier already exists
        if (clientSupplierRepository.existsByClient_ClientIdAndSupplierId(
                request.getClientId(), request.getSupplierId())) {
            throw new IllegalArgumentException(
                "Relationship already exists between client " + request.getClientId() +
                " and supplier " + request.getSupplierId());
        }

        // Fetch the Client entity
        Client client = clientRepository.findById(request.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + request.getClientId()));

        ClientSupplier clientSupplier = new ClientSupplier();
        clientSupplier.setClient(client);  // Set the actual Client entity
        clientSupplier.setSupplierId(request.getSupplierId());
        clientSupplier.setStatus(request.getStatus());
        clientSupplier.setSupplierName(request.getSupplierName());

        // Save to database using JPA/Hibernate ORM
        ClientSupplier savedClientSupplier = clientSupplierRepository.save(clientSupplier);
        
        // Convert to response DTO
        return convertToResponse(savedClientSupplier);
    }

    /**
     * Updates the status of an existing supplier-client relationship.
     *
     * @param request The relationship request containing clientID, supplierID, new status, and the supplier name
     * @return ClientSupplierResponse containing the updated relationship data
     * @throws RelationshipNotFoundException if the relationship is not found
     */
    public ClientSupplierResponse updateRelationshipStatus(ClientSupplierRequest request) {
        ClientSupplier clientSupplier = clientSupplierRepository
        .findByClient_ClientIdAndSupplierId(request.getClientId(), request.getSupplierId())
                .orElseThrow(() -> 
                new RelationshipNotFoundException(request.getClientId(), request.getSupplierId()));

        clientSupplier.setStatus(request.getStatus());

        ClientSupplier updatedClientSupplier = clientSupplierRepository.save(clientSupplier);

        return convertToResponse(updatedClientSupplier);
    }

    /**
     * Retrieves all supplier-client relationships for a specific client.
     * Converts each ClientSupplier entity into a ClientSupplierResponse DTO.
     *
     * @param clientId The ID of the client to look up.
     * @return A list of ClientSupplierResponse objects representing all relationships for the client.
     */
    public List<ClientSupplierResponse> getRelationshipsByClient(String clientId){
      // Query all relationships
      List<ClientSupplier> clientSupplier = clientSupplierRepository.findAllByClient_ClientId(clientId);

      // Convert List to a list of ClientSupplierResponse DTOs and return
      return clientSupplier.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves all supplier-client relationships for a specific supplier.
     * Converts each ClientSupplier entity into a ClientSupplierResponse DTO.
     *
     * @param supplierId The ID of the supplier to look up.
     * @return A list of ClientSupplierResponse objects representing all relationships for the supplier.
     */
    public List<ClientSupplierResponse> getRelationshipsBySupplier(String supplierId){
      // Query all relationships
      List<ClientSupplier> clientSupplier = clientSupplierRepository.findAllBySupplierId(supplierId);

      // Convert List to a list of ClientSupplierResponse DTOs and return
      return clientSupplier.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves the specific relationship between a client and supplier, using the IDs.
     * 
     * @param clientId The ID of the client
     * @param supplierId The ID of the supplier
     * @return ClientSupplierResponse
     * @throws RelationshipNotFoundException if the relationship is not found
     */
    public ClientSupplierResponse getRelationshipByClientIdSupplierId(String clientId, String supplierId) {
        ClientSupplier clientSupplier = clientSupplierRepository
            .findByClient_ClientIdAndSupplierId(clientId, supplierId)
            .orElseThrow(() -> 
                new RelationshipNotFoundException(clientId, supplierId));
        
        return convertToResponse(clientSupplier);
    }

    /**
     * Retrieves the specific relationship between a client and supplier, using clientId and userEmail
     *
     * @param clientId The ID of the client
     * @param userEmail The email of the user account
     * @return ClientSupplierResponse
     * @throws RelationshipNotFoundException if the relationship is not found
     */
    public ClientSupplierResponse getRelationshipByClientIdUserEmail(String clientId, String userEmail) {
        ClientSupplier relationship = userAccessRepository.findClientSupplierByUserEmailAndClientId(userEmail, clientId)
            .orElseThrow(() -> new RelationshipNotFoundException(clientId, userEmail));

        return convertToResponse(relationship);
    }

    /**
     * Retrieves all ClientSupplier entities for user email
     *
     * @param userEmail The email of the user account
     * @return ClientSupplierResponse
     * @throws RelationshipNotFoundException if the relationship is not found
     */
    public List<ClientSupplierResponse> getRelationshipsByUserEmail(String userEmail) {
        List<ClientSupplier> relationshipList = userAccessRepository
            .findAllClientSuppliersByUserEmail(userEmail);

        return relationshipList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    /**
     * Convert ClientSupplier entity to ClientSupplierResponse DTO
     */
    private ClientSupplierResponse convertToResponse(ClientSupplier clientSupplier) {
        ClientSupplierResponse response = new ClientSupplierResponse();
        response.setId(clientSupplier.getId());
        response.setClientId(clientSupplier.getClient().getClientId());
        response.setSupplierId(clientSupplier.getSupplierId());
        response.setStatus(clientSupplier.getStatus());
        response.setSupplierName(clientSupplier.getSupplierName());
        return response;
    }
}

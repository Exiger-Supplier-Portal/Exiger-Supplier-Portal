package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.model.Relationship;
import com.exiger.supplierportal.model.RelationshipID;
import com.exiger.supplierportal.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing supplier-client relationships.
 * Provides business logic for creating relationships using JPA/Hibernate ORM.
 */
@Service
@Transactional
public class RelationshipService {

    @Autowired
    private RelationshipRepository relationshipRepository;

    /**
     * Creates a new supplier-client relationship using ORM persistence.
     * 
     * @param request The relationship request containing clientID, supplierID, and status
     * @return RelationshipResponse containing the created relationship data
     * @throws IllegalArgumentException if a relationship already exists between the client and supplier
     */
    public RelationshipResponse createRelationship(RelationshipRequest request) {
        // Check if relationship already exists
        if (relationshipRepository.existsById_ClientIDAndId_SupplierID(
                request.getClientID(), request.getSupplierID())) {
            throw new IllegalArgumentException(
                "Relationship already exists between client " + request.getClientID() + 
                " and supplier " + request.getSupplierID());
        }

        // Create new relationship using ORM
        RelationshipID id = new RelationshipID();
        id.setClientID(request.getClientID());
        id.setSupplierID(request.getSupplierID());

        Relationship relationship = new Relationship();
        relationship.setId(id);
        relationship.setStatus(request.getStatus());

        // Save to database using JPA/Hibernate ORM
        Relationship savedRelationship = relationshipRepository.save(relationship);
        
        // Convert to response DTO
        return convertToResponse(savedRelationship);
    }

    /**
     * Convert Relationship entity to RelationshipResponse DTO
     */
    private RelationshipResponse convertToResponse(Relationship relationship) {
        RelationshipResponse response = new RelationshipResponse();
        response.setClientID(relationship.getId().getClientID());
        response.setSupplierID(relationship.getId().getSupplierID());
        response.setStatus(relationship.getStatus());
        return response;
    }
}
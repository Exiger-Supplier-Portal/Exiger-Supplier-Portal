package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.repository.RelationshipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RelationshipService.class)
@ActiveProfiles("test")
@Transactional 
class RelationshipServiceTest {

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateRelationship() {
        // First, create and persist a Client entity
        Client client = new Client();
        client.setClientID("C111");
        client.setClientName("Test Client");
        client.setClientEmail("test@client.com");
        Client savedClient = entityManager.persistAndFlush(client);
        
        // Create and persist a Supplier entity
        Supplier supplier = new Supplier();
        supplier.setSupplierID("S111");
        supplier.setSupplierName("Test Supplier");
        supplier.setSupplierEmail("test@supplier.com");
        Supplier savedSupplier = entityManager.persistAndFlush(supplier);

        // Now create the relationship request using the persisted entity IDs
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(savedClient.getClientID());
        request.setSupplierID(savedSupplier.getSupplierID());
        request.setStatus(SupplierStatus.INVITED);

        // Create the relationship
        RelationshipResponse response = relationshipService.createRelationship(request);
        
        // Verify the response
        assertThat(response).isNotNull();
        assertThat(response.getClientID()).isEqualTo(savedClient.getClientID());
        assertThat(response.getSupplierID()).isEqualTo(savedSupplier.getSupplierID());
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.INVITED);
    }
    
}
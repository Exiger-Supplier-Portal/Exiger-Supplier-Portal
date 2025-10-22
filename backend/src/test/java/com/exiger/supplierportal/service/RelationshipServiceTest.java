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

import java.util.List;

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
    void createRelationship_WhenValid_ShouldReturnRelationship() {
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

    @Test
    void getRelationshipsBySupplier_WhenValid_ShouldReturnRelationships() {
        // Client 1
        Client client1 = new Client();
        client1.setClientID("C111");
        client1.setClientName("Test Client 1");
        client1.setClientEmail("test@client1.com");
        Client savedClient1 = entityManager.persistAndFlush(client1);

        // Client 2
        Client client2 = new Client();
        client2.setClientID("C222");
        client2.setClientName("Test Client 2");
        client2.setClientEmail("test@client2.com");
        Client savedClient2 = entityManager.persistAndFlush(client2);

        // Supplier
        Supplier supplier = new Supplier();
        supplier.setSupplierID("S111");
        supplier.setSupplierName("Test Supplier");
        supplier.setSupplierEmail("test@supplier.com");
        Supplier savedSupplier = entityManager.persistAndFlush(supplier);

        // Relationship between Supplier & Client 1
        RelationshipRequest request1 = new RelationshipRequest();
        request1.setClientID(savedClient1.getClientID());
        request1.setSupplierID(savedSupplier.getSupplierID());
        request1.setStatus(SupplierStatus.INVITED);
        RelationshipResponse response1 = relationshipService.createRelationship(request1);

        // Relationship between Supplier & Client 2
        RelationshipRequest request2 = new RelationshipRequest();
        request2.setClientID(savedClient2.getClientID());
        request2.setSupplierID(savedSupplier.getSupplierID());
        request2.setStatus(SupplierStatus.ONBOARDING);
        RelationshipResponse response2 = relationshipService.createRelationship(request2);

        // Verify relationships were created correctly
        assertThat(response1).isNotNull();
        assertThat(response1.getClientID()).isEqualTo(savedClient1.getClientID());
        assertThat(response1.getSupplierID()).isEqualTo(savedSupplier.getSupplierID());
        assertThat(response1.getStatus()).isEqualTo(SupplierStatus.INVITED);

        assertThat(response2).isNotNull();
        assertThat(response2.getClientID()).isEqualTo(savedClient2.getClientID());
        assertThat(response2.getSupplierID()).isEqualTo(savedSupplier.getSupplierID());
        assertThat(response2.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);

        // Verify getRelationshipsBySupplier for the Supplier is correct
        List<RelationshipResponse> responseList = relationshipService.getRelationshipsBySupplier(savedSupplier.getSupplierID());
        assertThat(responseList).isNotNull().hasSize(2).extracting(RelationshipResponse::getClientID)
            .containsExactlyInAnyOrder(savedClient1.getClientID(), savedClient2.getClientID());
    }
    
}
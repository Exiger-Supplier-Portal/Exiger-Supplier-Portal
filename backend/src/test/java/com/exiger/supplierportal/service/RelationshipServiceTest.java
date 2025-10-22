package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Relationship;
import com.exiger.supplierportal.model.RelationshipID;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.repository.RelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private Client client1;
    private Client client2;
    private Supplier supplier1;
    private Supplier supplier2;
    private Relationship relationship;


    @BeforeEach
    void setUp() {
        client1 = persistClient("C111", "Test Client 1", "test@client1.com");
        client2 = persistClient("C222", "Test Client 2", "test@client2.com");
        supplier1 = persistSupplier("S111", "Test Supplier 1", "test@supplier1.com");
        supplier2 = persistSupplier("S222", "Test Supplier 2", "test@supplier2.com");


        RelationshipID id = new RelationshipID();
        id.setClientID(client2.getClientID());
        id.setSupplierID(supplier2.getSupplierID());

        relationship = new Relationship();
        relationship.setId(id);
        relationship.setClient(client2);
        relationship.setSupplier(supplier2);
        relationship.setStatus(SupplierStatus.INVITED);
        entityManager.persistAndFlush(relationship);
    }

    private Client persistClient(String id, String name, String email) {
        Client client = new Client();
        client.setClientID(id);
        client.setClientName(name);
        client.setClientEmail(email);
        return entityManager.persistAndFlush(client);
    }

    private Supplier persistSupplier(String id, String name, String email) {
        Supplier supplier = new Supplier();
        supplier.setSupplierID(id);
        supplier.setSupplierName(name);
        supplier.setSupplierEmail(email);
        return entityManager.persistAndFlush(supplier);
    }

    @Test
    void createRelationship_WhenValid_ShouldReturnRelationship() {
        // Now create the relationship request using the persisted entity IDs
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(client1.getClientID());
        request.setSupplierID(supplier1.getSupplierID());
        request.setStatus(SupplierStatus.INVITED);

        // Create the relationship
        RelationshipResponse response = relationshipService.createRelationship(request);
        
        // Verify the response
        assertThat(response).isNotNull();
        assertThat(response.getClientID()).isEqualTo(client1.getClientID());
        assertThat(response.getSupplierID()).isEqualTo(supplier1.getSupplierID());
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.INVITED);
    }

    @Test
    void createRelationship_WhenDuplicate_ShouldReturnIllegalArgumentException() {
        // Create relationship between client1 and supplier1
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(client1.getClientID());
        request.setSupplierID(supplier1.getSupplierID());
        request.setStatus(SupplierStatus.INVITED);

        // Create the relationship
        RelationshipResponse response = relationshipService.createRelationship(request);
        assertThat(response).isNotNull();

        // Attempt to create same relationship again
        RelationshipRequest request2 = new RelationshipRequest();
        request2.setClientID(client1.getClientID());
        request2.setSupplierID(supplier1.getSupplierID());
        request2.setStatus(SupplierStatus.ONBOARDING);

        // Create the relationship -- expecting IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            relationshipService.createRelationship(request2);
        });
    }

    @Test
    void createRelationship_WhenClientDoesNotExist_ShouldReturnIllegalArgumentException() {
        // Create relationship between client1 and supplier1
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("12345 NOT A CLIENT");
        request.setSupplierID(supplier1.getSupplierID());
        request.setStatus(SupplierStatus.ONBOARDING);

        // Create the relationship -- expecting IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            relationshipService.createRelationship(request);
        });
    }

    @Test
    void createRelationship_WhenSupplierDoesNotExist_ShouldReturnIllegalArgumentException() {
        // Create relationship between client1 and supplier1
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(client1.getClientID());
        request.setSupplierID("12345 NOT A SUPPLIER");
        request.setStatus(SupplierStatus.ONBOARDING);

        // Create the relationship -- expecting IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            relationshipService.createRelationship(request);
        });
    }

    @Test
    void getRelationshipsBySupplier_WhenValid_ShouldReturnRelationships() {
        // Relationship between Supplier & Client 1
        RelationshipRequest request1 = new RelationshipRequest();
        request1.setClientID(client1.getClientID());
        request1.setSupplierID(supplier1.getSupplierID());
        request1.setStatus(SupplierStatus.INVITED);
        RelationshipResponse response1 = relationshipService.createRelationship(request1);

        // Relationship between Supplier & Client 2
        RelationshipRequest request2 = new RelationshipRequest();
        request2.setClientID(client2.getClientID());
        request2.setSupplierID(supplier1.getSupplierID());
        request2.setStatus(SupplierStatus.ONBOARDING);
        RelationshipResponse response2 = relationshipService.createRelationship(request2);

        // Verify relationships were created correctly
        assertThat(response1).isNotNull();
        assertThat(response1.getClientID()).isEqualTo(client1.getClientID());
        assertThat(response1.getSupplierID()).isEqualTo(supplier1.getSupplierID());
        assertThat(response1.getStatus()).isEqualTo(SupplierStatus.INVITED);

        assertThat(response2).isNotNull();
        assertThat(response2.getClientID()).isEqualTo(client2.getClientID());
        assertThat(response2.getSupplierID()).isEqualTo(supplier1.getSupplierID());
        assertThat(response2.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);

        // Verify getRelationshipsBySupplier for the Supplier is correct
        List<RelationshipResponse> responseList = relationshipService.getRelationshipsBySupplier(supplier1.getSupplierID());
        assertThat(responseList).isNotNull().hasSize(2).extracting(RelationshipResponse::getClientID)
            .containsExactlyInAnyOrder(client1.getClientID(), client2.getClientID());
    }

    @Test
    void getRelationshipStatus_WhenExists_ShouldReturnStatus() {
        RelationshipResponse response = relationshipService.getRelationshipStatus(client2.getClientID(), supplier2.getSupplierID());

        assertThat(response).isNotNull();
        assertThat(response.getClientID()).isEqualTo(client2.getClientID());
        assertThat(response.getSupplierID()).isEqualTo(supplier2.getSupplierID());
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.INVITED);
    }

    @Test
    void getRelationshipStatus_WhenRelationshipDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        assertThatThrownBy(() ->
            relationshipService.getRelationshipStatus(client1.getClientID(), supplier2.getSupplierID()))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client1.getClientID())
            .hasMessageContaining(supplier2.getSupplierID());
    }

    @Test
    void getRelationshipStatus_WhenClientDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        assertThatThrownBy(() ->
            relationshipService.getRelationshipStatus("ABCDEFG", supplier2.getSupplierID()))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining("ABCDEFG")
            .hasMessageContaining(supplier2.getSupplierID());
    }

    @Test
    void getRelationshipStatus_WhenSupplierDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        assertThatThrownBy(() ->
            relationshipService.getRelationshipStatus(client1.getClientID(), "ABCDEFG"))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client1.getClientID())
            .hasMessageContaining("ABCDEFG");
    }

    @Test
    void updateRelationshipStatus_WhenValid_ShouldReturnUpdatedRelationship() {
        // Relationship between client2 and supplier2 with status INVITED exists
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(client2.getClientID());
        request.setSupplierID(supplier2.getSupplierID());
        request.setStatus(SupplierStatus.ONBOARDING);

        RelationshipResponse response = relationshipService.updateRelationshipStatus(request);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);

        // Verify persisted relationship was updated
        Relationship updated = relationshipRepository.findById_ClientIDAndId_SupplierID(client2.getClientID(), supplier2.getSupplierID()).orElseThrow();

        assertThat(updated.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);
    }

    @Test
    void updateRelationshipStatus_WhenRelationshipDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        // Request to update a non-existent relationship
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(client1.getClientID());
        request.setSupplierID(supplier2.getSupplierID());
        request.setStatus(SupplierStatus.ONBOARDING);

        assertThatThrownBy(() ->
            relationshipService.updateRelationshipStatus(request))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client1.getClientID())
            .hasMessageContaining(supplier2.getSupplierID());
    }

    @Test
    void updateRelationshipStatus_WhenClientDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID("I DONT EXIST");
        request.setSupplierID(supplier2.getSupplierID());
        request.setStatus(SupplierStatus.ONBOARDING);

        assertThatThrownBy(() ->
            relationshipService.updateRelationshipStatus(request))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining("I DONT EXIST")
            .hasMessageContaining(supplier2.getSupplierID());
    }

    @Test
    void updateRelationshipStatus_WhenSupplierDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        RelationshipRequest request = new RelationshipRequest();
        request.setClientID(client2.getClientID());
        request.setSupplierID("I DONT EXIST");
        request.setStatus(SupplierStatus.ONBOARDING);

        assertThatThrownBy(() ->
            relationshipService.updateRelationshipStatus(request))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client2.getClientID())
            .hasMessageContaining("I DONT EXIST");
    }
}
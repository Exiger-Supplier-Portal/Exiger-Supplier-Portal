package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.ClientSupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.request.UserAccountRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientSupplierResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccessResponse;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccountResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.repository.ClientSupplierRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClientSupplierServiceTest {

    @Autowired
    private ClientSupplierService clientSupplierService;

    @Autowired
    private ClientSupplierRepository clientSupplierRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private EntityManager entityManager;

    private Client client1;
    private Client client2;
    private ClientSupplier clientSupplier;

    @BeforeEach
    void setUp() {

        client1 = persistClient("C111", "Test Client 1", "test@client1.com");
        client2 = persistClient("C222", "Test Client 2", "test@client2.com");

        clientSupplier = new ClientSupplier();
        clientSupplier.setClient(client2);
        clientSupplier.setSupplierId("S123");
        clientSupplier.setSupplierName("Test Supplier 1");
        clientSupplier.setStatus(SupplierStatus.INVITED);
        entityManager.persist(clientSupplier);
        entityManager.flush();
    }

    private Client persistClient(String id, String name, String email) {
        Client client = new Client();
        client.setClientId(id);
        client.setClientName(name);
        client.setClientEmail(email);
        entityManager.persist(client);
        entityManager.flush();
        return client;
    }

    @Test
    void createRelationship_WhenValid_ShouldReturnRelationship() {
        // Now create the clientSupplier request using the persisted entity IDs
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId(client1.getClientId());
        request.setSupplierId("S456");
        request.setSupplierName("Test Supplier 2");
        request.setStatus(SupplierStatus.INVITED);

        // Create the clientSupplier
        ClientSupplierResponse response = clientSupplierService.createRelationship(request);
        
        // Verify the response
        assertThat(response).isNotNull();
        assertThat(response.getClientId()).isEqualTo(client1.getClientId());
        assertThat(response.getSupplierId()).isEqualTo("S456");
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.INVITED);
    }

    @Test
    void createRelationship_WhenDuplicate_ShouldReturnIllegalArgumentException() {
        // Create clientSupplier between client1 and supplier1
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId(client1.getClientId());
        request.setSupplierId("S456");
        request.setSupplierName("Test Supplier 2");
        request.setStatus(SupplierStatus.INVITED);
        // Create the clientSupplier

        ClientSupplierResponse response = clientSupplierService.createRelationship(request);
        assertThat(response).isNotNull();

        // Attempt to create same clientSupplier again
        ClientSupplierRequest request2 = new ClientSupplierRequest();
        request2.setClientId(client1.getClientId());
        request2.setSupplierId("S456");
        request2.setSupplierName("Test Supplier 2");
        request2.setStatus(SupplierStatus.ONBOARDING);

        // Create the clientSupplier -- expecting IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            clientSupplierService.createRelationship(request2);
        });
    }

    @Test
    void createRelationship_WhenClientDoesNotExist_ShouldReturnIllegalArgumentException() {
        // Create clientSupplier between client1 and supplier1
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("12345 NOT A CLIENT");
        request.setSupplierId("S456");
        request.setSupplierName("Test Supplier 2");
        request.setStatus(SupplierStatus.ONBOARDING);

        // Create the clientSupplier -- expecting IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            clientSupplierService.createRelationship(request);
        });
    }

    @Test
    void getRelationshipsBySupplier_WhenValid_ShouldReturnRelationships() {
        // ClientSupplier between Supplier & Client 1
        ClientSupplierRequest request1 = new ClientSupplierRequest();
        request1.setClientId(client1.getClientId());
        request1.setSupplierId("S456");
        request1.setSupplierName("Test Supplier 456");
        request1.setStatus(SupplierStatus.INVITED);
        ClientSupplierResponse response1 = clientSupplierService.createRelationship(request1);

        // ClientSupplier between Supplier & Client 2
        ClientSupplierRequest request2 = new ClientSupplierRequest();
        request2.setClientId(client2.getClientId());
        request2.setSupplierId("S456");
        request2.setSupplierName("Test Supplier 456");
        request2.setStatus(SupplierStatus.ONBOARDING);
        ClientSupplierResponse response2 = clientSupplierService.createRelationship(request2);

        // Verify relationships were created correctly
        assertThat(response1).isNotNull();
        assertThat(response1.getClientId()).isEqualTo(client1.getClientId());
        assertThat(response1.getSupplierId()).isEqualTo("S456");
        assertThat(response1.getStatus()).isEqualTo(SupplierStatus.INVITED);

        assertThat(response2).isNotNull();
        assertThat(response2.getClientId()).isEqualTo(client2.getClientId());
        assertThat(response2.getSupplierId()).isEqualTo("S456");
        assertThat(response2.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);

        // Verify getRelationshipsBySupplier for the Supplier is correct
        List<ClientSupplierResponse> responseList = clientSupplierService.getRelationshipsBySupplier("S456");
        assertThat(responseList).isNotNull().hasSize(2).extracting(ClientSupplierResponse::getClientId)
            .containsExactlyInAnyOrder(client1.getClientId(), client2.getClientId());
    }

    @Test
    void getRelationship_WhenExists_ShouldReturnRelationship() {
        ClientSupplierResponse response = clientSupplierService.getRelationshipByClientIdSupplierId(client2.getClientId(), "S123");

        assertThat(response).isNotNull();
        assertThat(response.getClientId()).isEqualTo(client2.getClientId());
        assertThat(response.getSupplierId()).isEqualTo("S123");
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.INVITED);
    }

    @Test
    void getRelationship_WhenRelationshipDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        assertThatThrownBy(() ->
            clientSupplierService.getRelationshipByClientIdSupplierId(client1.getClientId(), "S123"))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client1.getClientId())
            .hasMessageContaining("S123");
    }

    @Test
    void getRelationship_WhenClientDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        assertThatThrownBy(() ->
            clientSupplierService.getRelationshipByClientIdSupplierId("ABCDEFG", "S123"))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining("ABCDEFG")
            .hasMessageContaining("S123");
    }

    @Test
    void getRelationship_WhenSupplierDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        assertThatThrownBy(() ->
            clientSupplierService.getRelationshipByClientIdSupplierId(client1.getClientId(), "ABCDEFG"))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client1.getClientId())
            .hasMessageContaining("ABCDEFG");
    }

    @Test
    void updateRelationshipStatus_WhenValid_ShouldReturnUpdatedRelationship() {
        // ClientSupplier between client2 and supplier2 with status INVITED exists
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId(client2.getClientId());
        request.setSupplierId("S123");
        request.setStatus(SupplierStatus.ONBOARDING);

        ClientSupplierResponse response = clientSupplierService.updateRelationshipStatus(request);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);

        // Verify persisted clientSupplier was updated
        ClientSupplier updated = clientSupplierRepository.findByClient_ClientIdAndSupplierId(client2.getClientId(), "S123").orElseThrow();

        assertThat(updated.getStatus()).isEqualTo(SupplierStatus.ONBOARDING);
    }

    @Test
    void updateRelationshipStatus_WhenRelationshipDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        // Request to update a non-existent clientSupplier
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId(client1.getClientId());
        request.setSupplierId("S123");
        request.setStatus(SupplierStatus.ONBOARDING);

        assertThatThrownBy(() ->
            clientSupplierService.updateRelationshipStatus(request))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client1.getClientId())
            .hasMessageContaining("S123");
    }

    @Test
    void updateRelationshipStatus_WhenClientDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId("I DONT EXIST");
        request.setSupplierId("S123");
        request.setStatus(SupplierStatus.ONBOARDING);

        assertThatThrownBy(() ->
            clientSupplierService.updateRelationshipStatus(request))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining("I DONT EXIST")
            .hasMessageContaining("S123");
    }

    @Test
    void updateRelationshipStatus_WhenSupplierDoesNotExist_ShouldReturnRelationshipNotFoundException() {
        ClientSupplierRequest request = new ClientSupplierRequest();
        request.setClientId(client2.getClientId());
        request.setSupplierId("I DONT EXIST");
        request.setStatus(SupplierStatus.ONBOARDING);

        assertThatThrownBy(() ->
            clientSupplierService.updateRelationshipStatus(request))
            .isInstanceOf(RelationshipNotFoundException.class)
            .hasMessageContaining(client2.getClientId())
            .hasMessageContaining("I DONT EXIST");
    }

    @Test
    void getRelationshipsByUserEmail_WhenValid_ShouldReturnRelationships() {
        UserAccountRequest request = new UserAccountRequest();
        request.setUserEmail("bob@example.com");
        request.setFirstName("bob");
        request.setLastName("joe");

        UserAccountResponse response = userAccountService.createUser(request);
        assertThat(response).isNotNull();
        assertThat(response.getUserEmail()).isEqualTo("bob@example.com");

        UserAccessRequest request2 = new UserAccessRequest();
        request2.setClientSupplierId(clientSupplier.getId());
        request2.setUserEmail(response.getUserEmail());

        UserAccessResponse response2 = userAccessService.createUserAccess(request2);
        assertThat(response2).isNotNull();
        assertThat(response2.getUserEmail()).isEqualTo("bob@example.com");

        List<ClientSupplierResponse> relationships = clientSupplierService.getRelationshipsByUserEmail("bob@example.com");
        assertThat(relationships).isNotNull().hasSize(1);
    }
}
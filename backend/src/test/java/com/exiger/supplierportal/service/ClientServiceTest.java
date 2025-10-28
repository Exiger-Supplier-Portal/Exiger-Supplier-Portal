package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.ClientRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.ClientResponse;
import com.exiger.supplierportal.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll(); // clean slate for each test
    }

    @Test
    void testCreateClient() {
        // Create client request
        ClientRequest request = new ClientRequest();
        request.setClientId("C111");
        request.setClientName("Test Client");
        request.setClientEmail("test@client.com");

        // Create the client
        ClientResponse response = clientService.createClient(request);

        // Verify the response
        assertThat(response).isNotNull();
        assertThat(response.getClientID()).isEqualTo("C111");
        assertThat(response.getClientName()).isEqualTo("Test Client");
        assertThat(response.getClientEmail()).isEqualTo("test@client.com");

        // Verify client was persisted to database
        assertThat(clientRepository.existsById("C111")).isTrue();
    }

    @Test
    void testCreateClient_WithDuplicateID_ShouldThrowException() {
        // First, create and persist a Client entity
        ClientRequest request1 = new ClientRequest();
        request1.setClientId("C111");
        request1.setClientName("Test Client");
        request1.setClientEmail("test@client.com");
        clientService.createClient(request1);

        // Try to create another client with the same ID
        ClientRequest request2 = new ClientRequest();
        request2.setClientId("C111");
        request2.setClientName("Another Client");
        request2.setClientEmail("another@client.com");

        // Verify that exception is thrown
        assertThatThrownBy(() -> clientService.createClient(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Client already exists with ID: C111");
    }

    @Test
    void testGetAllClientIDs() {
        // Create and persist multiple clients
        ClientRequest request1 = new ClientRequest();
        request1.setClientId("C111");
        request1.setClientName("Test Client 1");
        request1.setClientEmail("test@client1.com");
        clientService.createClient(request1);

        ClientRequest request2 = new ClientRequest();
        request2.setClientId("C222");
        request2.setClientName("Test Client 2");
        request2.setClientEmail("test@client2.com");
        clientService.createClient(request2);

        // Get all client IDs
        var clientIds = clientService.getAllClientIds();

        // Verify the results
        assertThat(clientIds).isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder("C111", "C222");
    }
}
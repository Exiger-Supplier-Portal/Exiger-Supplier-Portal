package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.InviteRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.InviteResponse;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.repository.ClientRepository;
import com.exiger.supplierportal.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(InviteService.class)
@ActiveProfiles("test")
@Transactional
public class InviteServiceTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private TestEntityManager entityManager;

    private Client client;
    private Registration validRegistration;
    private final String baseURL = "https://portal.exiger.com";

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setClientID("C111");
        client.setClientName("Test Client 1");
        client.setClientEmail("test@client1.com");
        client = entityManager.persistAndFlush(client);

        // Inject the base URL
        ReflectionTestUtils.setField(inviteService, "frontendBaseUrl", baseURL);
    }

    @Test
    void createInvite_WithValidClient_ShouldReturnInviteResponse() {
        // Given
        InviteRequest request = new InviteRequest();
        request.setClientID(client.getClientID());
        request.setSupplierEmail("test@supplier.com");

        // When
        InviteResponse response = inviteService.createInvite(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getRegistrationUrl()).startsWith(baseURL + "/register?token=");
        assertThat(response.getExpiresAt().isAfter(Instant.now()));
    }

    @Test
    void createInvite_WithNonExistentClient_ShouldThrowException() {
        // Given
        InviteRequest request = new InviteRequest();
        request.setClientID("MISSING-CLIENT");
        request.setSupplierEmail("test@supplier.com");

        // When & Then
        assertThatThrownBy(() -> inviteService.createInvite(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Client not found");
    }

    @Test
    void createInvite_ShouldPersistRegistrationRecord() {
        InviteRequest request = new InviteRequest();
        request.setClientID(client.getClientID());
        request.setSupplierEmail("test@supplier.com");

        InviteResponse response = inviteService.createInvite(request);

        UUID token = UUID.fromString(response.getRegistrationUrl().split("token=")[1]);
        Optional<Registration> result = registrationRepository.findByTokenAndExpirationAfter(token, Instant.now());
        assertThat(result).isNotEmpty();
        assertThat(result.get().getSupplierEmail()).isEqualTo("test@supplier.com");
    }
}

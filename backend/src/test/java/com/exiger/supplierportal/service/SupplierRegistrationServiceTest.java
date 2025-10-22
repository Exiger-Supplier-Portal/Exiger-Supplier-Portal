package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RegistrationRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RegistrationResponse;
import com.exiger.supplierportal.exception.RegistrationException;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.model.SupplierRegistration;
import com.exiger.supplierportal.repository.SupplierRegistrationRepository;
import com.exiger.supplierportal.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class SupplierRegistrationServiceTest {

    @Autowired
    private SupplierRegistrationRepository supplierRegistrationRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UUID validToken;
    private Client testClient;
    private SupplierRegistration validRegistration;

    @BeforeEach
    void setUp() {
        validToken = UUID.randomUUID();
        
        testClient = new Client();
        testClient.setClientID("test-client");
        testClient.setClientName("Test Client");
        testClient.setClientEmail("test@client.com");
        entityManager.persistAndFlush(testClient);

        validRegistration = new SupplierRegistration();
        validRegistration.setToken(validToken);
        validRegistration.setSupplierEmail("test@supplier.com");
        validRegistration.setExpiration(LocalDateTime.now().plusHours(24));
        validRegistration.setClient(testClient);
        entityManager.persistAndFlush(validRegistration);
    }

    @Test
    void findByTokenAndExpirationAfter_WithValidToken_ShouldReturnRegistration() {
        // When
        Optional<SupplierRegistration> result = supplierRegistrationRepository
                .findByTokenAndExpirationAfter(validToken, LocalDateTime.now());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(validToken);
        assertThat(result.get().getSupplierEmail()).isEqualTo("test@supplier.com");
    }

    @Test
    void findByTokenAndExpirationAfter_WithExpiredToken_ShouldReturnEmpty() {
        // Given
        SupplierRegistration expiredRegistration = new SupplierRegistration();
        expiredRegistration.setToken(UUID.randomUUID());
        expiredRegistration.setSupplierEmail("expired@supplier.com");
        expiredRegistration.setExpiration(LocalDateTime.now().minusHours(1)); // Expired
        expiredRegistration.setClient(testClient);
        entityManager.persistAndFlush(expiredRegistration);

        // When
        Optional<SupplierRegistration> result = supplierRegistrationRepository
                .findByTokenAndExpirationAfter(expiredRegistration.getToken(), LocalDateTime.now());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByTokenAndExpirationAfter_WithInvalidToken_ShouldReturnEmpty() {
        // Given
        UUID invalidToken = UUID.randomUUID();

        // When
        Optional<SupplierRegistration> result = supplierRegistrationRepository
                .findByTokenAndExpirationAfter(invalidToken, LocalDateTime.now());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findBySupplierEmail_WithExistingSupplier_ShouldReturnSupplier() {
        // Given
        Supplier existingSupplier = new Supplier();
        existingSupplier.setSupplierID("existing-supplier-id");
        existingSupplier.setSupplierEmail("test@supplier.com");
        existingSupplier.setSupplierName("Existing Company");
        entityManager.persistAndFlush(existingSupplier);

        // When
        Optional<Supplier> result = supplierRepository.findBySupplierEmail("test@supplier.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getSupplierEmail()).isEqualTo("test@supplier.com");
        assertThat(result.get().getSupplierID()).isEqualTo("existing-supplier-id");
    }

    @Test
    void findBySupplierEmail_WithNonExistingSupplier_ShouldReturnEmpty() {
        // When
        Optional<Supplier> result = supplierRepository.findBySupplierEmail("nonexistent@supplier.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void deleteByToken_ShouldDeleteRegistration() {
        // Given
        assertThat(supplierRegistrationRepository.findByTokenAndExpirationAfter(validToken, LocalDateTime.now()))
                .isPresent();

        // When
        supplierRegistrationRepository.deleteByToken(validToken);

        // Then
        assertThat(supplierRegistrationRepository.findByTokenAndExpirationAfter(validToken, LocalDateTime.now()))
                .isEmpty();
    }
}
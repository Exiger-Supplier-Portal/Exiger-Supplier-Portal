package com.exiger.supplierportal.service;

import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.model.Registration;
import com.exiger.supplierportal.repository.RegistrationRepository;
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

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class RegistrationServiceTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UUID validToken;
    private Client testClient;
    private Registration validRegistration;

    @BeforeEach
    void setUp() {
        validToken = UUID.randomUUID();
        
        testClient = new Client();
        testClient.setClientID("test-client");
        testClient.setClientName("Test Client");
        testClient.setClientEmail("test@client.com");
        entityManager.persistAndFlush(testClient);

        validRegistration = new Registration();
        validRegistration.setToken(validToken);
        validRegistration.setSupplierEmail("test@supplier.com");
        validRegistration.setExpiration(LocalDateTime.now().plusHours(24));
        validRegistration.setClient(testClient);
        entityManager.persistAndFlush(validRegistration);
    }

    @Test
    void findByTokenAndExpirationAfter_WithValidToken_ShouldReturnRegistration() {
        // When
        Optional<Registration> result = registrationRepository
                .findByTokenAndExpirationAfter(validToken, LocalDateTime.now());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(validToken);
        assertThat(result.get().getSupplierEmail()).isEqualTo("test@supplier.com");
    }

    @Test
    void findByTokenAndExpirationAfter_WithExpiredToken_ShouldReturnEmpty() {
        // Given
        Registration expiredRegistration = new Registration();
        expiredRegistration.setToken(UUID.randomUUID());
        expiredRegistration.setSupplierEmail("expired@supplier.com");
        expiredRegistration.setExpiration(LocalDateTime.now().minusHours(1)); // Expired
        expiredRegistration.setClient(testClient);
        entityManager.persistAndFlush(expiredRegistration);

        // When
        Optional<Registration> result = registrationRepository
                .findByTokenAndExpirationAfter(expiredRegistration.getToken(), LocalDateTime.now());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByTokenAndExpirationAfter_WithInvalidToken_ShouldReturnEmpty() {
        // Given
        UUID invalidToken = UUID.randomUUID();

        // When
        Optional<Registration> result = registrationRepository
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
        assertThat(registrationRepository.findByTokenAndExpirationAfter(validToken, LocalDateTime.now()))
                .isPresent();

        // When
        registrationRepository.deleteByToken(validToken);

        // Then
        assertThat(registrationRepository.findByTokenAndExpirationAfter(validToken, LocalDateTime.now()))
                .isEmpty();
    }
}
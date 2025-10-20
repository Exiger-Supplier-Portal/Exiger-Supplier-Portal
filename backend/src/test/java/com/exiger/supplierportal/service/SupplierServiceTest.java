package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.SupplierRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.SupplierResponse;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(SupplierService.class)
@ActiveProfiles("test")
@Transactional 
class SupplierServiceTest {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void createSupplier_WithValidRequest_ShouldReturnSupplierResponse() {
        // Given
        SupplierRequest request = new SupplierRequest();
        request.setSupplierID("S111");
        request.setSupplierName("Test Supplier");
        request.setSupplierEmail("test@supplier.com");

        // When
        SupplierResponse response = supplierService.createSupplier(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getSupplierName()).isEqualTo("Test Supplier");
        assertThat(response.getSupplierEmail()).isEqualTo("test@supplier.com");
        assertThat(response.getSupplierID()).isNotNull();
        assertThat(response.getSupplierID()).isNotEmpty();

        // Verify supplier was saved to database
        Optional<Supplier> savedSupplierOpt = supplierRepository.findBySupplierEmail("test@supplier.com");
        assertThat(savedSupplierOpt).isPresent();
        Supplier savedSupplier = savedSupplierOpt.get();
        assertThat(savedSupplier.getSupplierName()).isEqualTo("Test Supplier");
        assertThat(savedSupplier.getSupplierEmail()).isEqualTo("test@supplier.com");
        assertThat(savedSupplier.getSupplierID()).isEqualTo(response.getSupplierID());
    }

    @Test
    void createSupplier_WithDuplicateEmail_ShouldThrowException() {
        // Given - Create an existing supplier
        Supplier existingSupplier = new Supplier();
        existingSupplier.setSupplierID("E111");
        existingSupplier.setSupplierName("Existing Supplier");
        existingSupplier.setSupplierEmail("existing@supplier.com");
        entityManager.persistAndFlush(existingSupplier);

        SupplierRequest request = new SupplierRequest();
        request.setSupplierID("N111");
        request.setSupplierName("New Supplier");
        request.setSupplierEmail("existing@supplier.com"); // Same email as existing

        // When & Then
        assertThatThrownBy(() -> supplierService.createSupplier(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Supplier already created with given email")
                .hasMessageContaining("existing@supplier.com");
    }

    @Test
    void createSupplier_WithSpecialCharactersInName_ShouldSucceed() {
        // Given
        SupplierRequest request = new SupplierRequest();
        request.setSupplierID("123AB");
        request.setSupplierName("Test & Co. Ltd. (Inc.)");
        request.setSupplierEmail("test@supplier.com");

        // When
        SupplierResponse response = supplierService.createSupplier(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getSupplierName()).isEqualTo("Test & Co. Ltd. (Inc.)");
        assertThat(response.getSupplierEmail()).isEqualTo("test@supplier.com");
    }

    @Test
    void createSupplier_WithLongName_ShouldSucceed() {
        // Given
        String longName = "A".repeat(255); // Assuming reasonable max length
        SupplierRequest request = new SupplierRequest();
        request.setSupplierID("ABCDE");
        request.setSupplierName(longName);
        request.setSupplierEmail("test@supplier.com");

        // When
        SupplierResponse response = supplierService.createSupplier(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getSupplierName()).isEqualTo(longName);
        assertThat(response.getSupplierEmail()).isEqualTo("test@supplier.com");
    }

    @Test
    void createSupplier_WithValidEmailFormats_ShouldSucceed() {
        // Given
        String[] validEmails = {
            "test@example.com",
            "user.name@domain.co.uk",
            "test+tag@example.org",
            "123@test.com"
        };

        for (int i = 0; i < validEmails.length; i++) {
            SupplierRequest request = new SupplierRequest();
            request.setSupplierID("987" + i);
            request.setSupplierName("Test Supplier " + i);
            request.setSupplierEmail(validEmails[i]);

            // When
            SupplierResponse response = supplierService.createSupplier(request);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getSupplierEmail()).isEqualTo(validEmails[i]);
        }
    }
}

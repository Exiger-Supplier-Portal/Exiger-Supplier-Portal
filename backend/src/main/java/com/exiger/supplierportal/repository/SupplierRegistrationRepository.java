package com.exiger.supplierportal.repository;

import com.exiger.supplierportal.model.SupplierRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing supplier registration
 * Inherits CRUD operations from JpaRepository
 */
@Repository
public interface SupplierRegistrationRepository extends JpaRepository<SupplierRegistration, Long> {

    /**
     * Find a supplier registration by token and check if it's not expired.
     * Uses Spring Data JPA's automatic query generation.
     * 
     * @param token The registration token
     * @param expiration The expiration time to check against
     * @return Optional SupplierRegistration if found and not expired
     */
    Optional<SupplierRegistration> findByTokenAndExpirationAfter(UUID token, LocalDateTime expiration);

    /**
     * Delete a supplier registration by token.
     * Uses Spring Data JPA's automatic query generation.
     * 
     * @param token The registration token to delete
     */
    void deleteByToken(UUID token);
}

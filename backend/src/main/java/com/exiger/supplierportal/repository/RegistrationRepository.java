package com.exiger.supplierportal.repository;

import com.exiger.supplierportal.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing supplier registration
 * Inherits CRUD operations from JpaRepository
 */
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /**
     * Find a supplier registration by token and check if it's not expired.
     * Uses Spring Data JPA's automatic query generation.
     * 
     * @param token The registration token
     * @param expiration The expiration time to check against
     * @return Optional SupplierRegistration if found and not expired
     */
    Optional<Registration> findByTokenAndExpirationAfter(UUID token, Instant expiration);

    /**
     * Delete a supplier registration by token.
     * Uses Spring Data JPA's automatic query generation.
     * 
     * @param token The registration token to delete
     */
    void deleteByToken(UUID token);
}

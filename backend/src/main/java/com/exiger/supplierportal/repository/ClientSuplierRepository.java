package com.exiger.supplierportal.repository;

import com.exiger.supplierportal.model.ClientSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing relationships between clients and suppliers.
 * Provides methods to find relationships by client ID or supplier ID,
 * and inherits standard CRUD operations from JpaRepository.
 */
@Repository
public interface ClientSuplierRepository extends JpaRepository<ClientSupplier, String> {
    /**
     * Find client-supplier relationship by clientID and supplierID
     *
     * @param clientId ID of client
     * @param supplierId ID of supplier
     * @return Optional containing the ClientSupplier if found
     */
    Optional<ClientSupplier> findByClient_ClientIdAndSupplierId(String clientId, String supplierId);

    /**
     * Checks if a client-supplier relationship exists by clientID and supplierID
     * @param clientId ID of client
     * @param supplierId ID of supplier
     * @return true if the client-supplier relationship exists, false otherwise
     */
    boolean existsByClient_ClientIdAndSupplierId(String clientId, String supplierId);

    /**
     * Get all relationships with clientId
     * @param clientId ID of client
     * @return List of ClientSupplier objects that clientId belongs to
     */
    List<ClientSupplier> findAllByClient_ClientId(String clientId);

    /**
     * Get all relationships with supplierId
     * @param supplierId ID of supplier
     * @return List of ClientSupplier objects that supplierId belongs to
     */
    List<ClientSupplier> findAllBySupplierId(String supplierId);
}

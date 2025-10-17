package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.Relationship;
import com.exiger.supplierportal.model.RelationshipID;
import java.util.List;

/**
 * Repository for managing relationships between clients and suppliers.
 * Provides methods to find relationships by client ID or supplier ID,
 * and inherits standard CRUD operations from JpaRepository.
 */
@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipID> {

    /**
     * Finds all Relationship entries for a given client.
     * 
     * @param clientID The ID of the client to look up.
     * @return A list of Relationship objects where the clientID matches.
     */    
    List<Relationship> findById_ClientID(String clientID);

    /**
     * Finds all Relationship entries for a given supplier.
     * 
     * @param supplierID The ID of the supplier to look up.
     * @return A list of Relationship objects where the supplierID matches.
     */
    List<Relationship> findById_SupplierID(String supplierID);

    /**
     * Check if a relationship already exists between client and supplier.
     * Uses Spring Data JPA's automatic query generation.
     */
    boolean existsById_ClientIDAndId_SupplierID(String clientID, String supplierID);
}
package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.Supplier;
import java.util.List;

/**
 * Repository for managing clients. Has method that will return a List of all
 *  the Supplier IDs. Inherits basic CRUD operations from JpaRepository.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String>{
    
    // Query to return a List of all the supplierIDs
    @Query("SELECT s.supplierID FROM Supplier s")
    List<String> findAllSupplierIDs();

    /**
     * Check if a supplier already exists given supplierEmail (field in supplier model).
     * Uses Spring Data JPA's automatic query generation.
     * 
     * @param supplierEmail The email of the supplier to check.
     * @return true if a supplier with given email already exists, false otherwise.
     */
    boolean existsBySupplierEmail(String supplierEmail);

}
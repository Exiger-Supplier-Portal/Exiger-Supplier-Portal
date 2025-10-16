package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.Client;
import java.util.List;


/**
 * Repository for managing clients. Has method that will return a List of all
 *  the client IDs. Inherits basic CRUD operations from JpaRepository.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    // Query to return a List of all the clientIDs
    @Query("SELECT c.clientID from Client c")
    List<Long> findAllClientIDs();
}
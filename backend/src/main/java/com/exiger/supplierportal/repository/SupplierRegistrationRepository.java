package com.exiger.supplierportal.repository;

import com.exiger.supplierportal.model.SupplierRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing supplier registration
 * Inherits CRUD operations from JpaRepository
 */
public interface SupplierRegistrationRepository extends JpaRepository<SupplierRegistration, Long> {

}

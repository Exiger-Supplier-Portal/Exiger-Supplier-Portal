package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
    
    //Will add custom query methods here
}
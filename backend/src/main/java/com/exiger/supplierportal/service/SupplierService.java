package com.exiger.supplierportal.service;

import com.exiger.supplierportal.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 * Service for managing Supplier entities.
 * Provides business logic for retrieving supplier information.
 */
@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Retrieves all supplier IDs from the database.
     *
     * @return A list of supplier IDs.
     */
    public List<String> getAllSupplierIDs() {
        return supplierRepository.findAllSupplierIDs();
    }
}

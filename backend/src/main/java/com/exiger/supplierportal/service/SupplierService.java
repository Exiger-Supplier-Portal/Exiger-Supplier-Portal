package com.exiger.supplierportal.service;

import com.exiger.supplierportal.repository.SupplierRepository;
import com.exiger.supplierportal.dto.clientsupplier.response.SupplierResponse;
import com.exiger.supplierportal.model.Supplier;
import com.exiger.supplierportal.dto.clientsupplier.request.SupplierRequest;
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

    /**
     * Creates a new supplier using ORM persistence.
     * 
     * @param request The SupplierRequest containing supplierName and supplierEmail
     * @return SupplierResponse containing the created relationship data
     * @throws IllegalArgumentException if already a supplier object with given supplierEmail
     */
    public SupplierResponse createSupplier(SupplierRequest request) {
        // Check if supplier already exists
        if (supplierRepository.existsBySupplierEmail(
                request.getSupplierEmail())) {
            throw new IllegalArgumentException(
                "Supplier already created with given email" + request.getSupplierEmail());
        }
        // Create new relationship using ORM
        Supplier supplier = new Supplier();
        supplier.setSupplierEmail(request.getSupplierEmail());
        supplier.setSupplierName(request.getSupplierName());

        // Save to database using JPA/Hibernate ORM
        Supplier savedSupplier = supplierRepository.save(supplier);
        
        // Convert to response DTO
        return convertToResponse(savedSupplier);

    }

    /**
     * Convert Supplier entity to SupplierResponse DTO
     */
    private SupplierResponse convertToResponse(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setSupplierID(supplier.getSupplierID());
        response.setSupplierEmail(supplier.getSupplierEmail());
        response.setSupplierName(supplier.getSupplierName());
        return response;
    }
}

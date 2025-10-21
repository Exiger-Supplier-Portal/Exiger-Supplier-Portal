package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.response.SupplierRegistrationResponse;
import com.exiger.supplierportal.model.SupplierRegistration;
import com.exiger.supplierportal.repository.SupplierRegistrationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing supplier registrations
 * Business logic for adding registrations and transferring from registration to full account and relationship
 */
@Service
@Transactional
public class SupplierRegistrationService {

    @Autowired
    private SupplierRegistrationRepository supplierRegistrationRepository;
}

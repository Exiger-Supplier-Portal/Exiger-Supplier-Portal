package com.exiger.supplierportal.service;

import com.exiger.supplierportal.repository.ClientRepository;
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
public class ClientService {

    @Autowired
    private ClientService clientRepository;

    /**
     * Retrieves all client IDs from the database.
     *
     * @return A list of client IDs.
     */
    public List<Long> getAllClientIDs() {
        return clientRepository.findAllClientIDs();
    }
}


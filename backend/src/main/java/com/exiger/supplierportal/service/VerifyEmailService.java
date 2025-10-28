package com.exiger.supplierportal.service;

import com.exiger.supplierportal.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for checking whether a user email exists.
 * Provides business logic for detecting whether a user already exists.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class VerifyEmailService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    /**
     * Check if the user email exists in the Users table.
     * @param email email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userAccountRepository.existsByUserEmail(email);
    }
}

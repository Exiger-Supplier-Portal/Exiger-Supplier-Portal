package com.exiger.supplierportal.service;

import com.exiger.supplierportal.repository.UserAccountRepository;
import com.exiger.supplierportal.repository.RegistrationRepository;
import com.exiger.supplierportal.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

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

    @Autowired
    private RegistrationRepository registrationRepository;

    /**
     * Validate registration token by checking Registration repository and expiration.
     * Throws RegistrationException if token is invalid or expired.
     */
    public void validateRegistrationToken(UUID token) {
        if (registrationRepository.findByTokenAndExpirationAfter(token, Instant.now()).isEmpty()) {
            throw new RegistrationException("Invalid or expired registration token");
        }
    }

    /**
     * Check if the user email exists in the Users table.
     * @param email email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userAccountRepository.existsByUserEmail(email);
    }

}

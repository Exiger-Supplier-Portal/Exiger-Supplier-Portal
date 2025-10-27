package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.UserAccountRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccountResponse;
import com.exiger.supplierportal.model.UserAccount;
import com.exiger.supplierportal.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 * Service for managing UserAccount entities.
 * Provides business logic for retrieving supplier information.
 */
@Service
@Transactional
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    /**
     * Retrieves all user emails from the database
     *
     * @return A list of user emails
     */
    public List<String> getAllUserEmails() {
        return userAccountRepository.findAllUserEmails();
    }

    /**
     * Creates a new user account using ORM persistence.
     * 
     * @param request The UserAccountRequest containing userEmail, firstName, lastName
     * @return UserAccountResponse containing the created account data
     * @throws IllegalArgumentException if an account already exists with userEmail
     */
    public UserAccountResponse createUser(UserAccountRequest request) {
        // Check if user already exists
        if (userAccountRepository.existsByUserEmail(
                request.getUserEmail())) {
            throw new IllegalArgumentException(
                "User account already created with given email" + request.getUserEmail());
        }
        // Create new relationship using ORM
        UserAccount user = new UserAccount();
        user.setUserEmail(request.getUserEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Save to database using JPA/Hibernate ORM
        UserAccount savedUser = userAccountRepository.save(user);
        
        // Convert to response DTO
        return convertToResponse(savedUser);
    }

    /**
     * Convert UserAccount entity to UserAccountResponse DTO
     */
    private UserAccountResponse convertToResponse(UserAccount user) {
        UserAccountResponse response = new UserAccountResponse();
        response.setUserEmail(user.getUserEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return response;
    }
}

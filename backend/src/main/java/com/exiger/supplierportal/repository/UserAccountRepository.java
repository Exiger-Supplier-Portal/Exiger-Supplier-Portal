package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.UserAccount;
import java.util.List;
import java.util.Optional;


/**
 * Repository for managing user accounts
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    /**
     * Get all user emails in UserAccount
     *
     * @return List of email strings
     */
    @Query("SELECT u.userEmail FROM UserAccount u")
    List<String> findAllUserEmails();

    /**
     * Check if an account already exists given userEmail (field in supplier model).
     * Uses Spring Data JPA's automatic query generation.
     *
     * @param userEmail The email to check.
     * @return true if a user with given email exists, false otherwise.
     */
    boolean existsByUserEmail(String userEmail);

    /**
     * Find a user by their email.
     *
     * @param userEmail The email of the user to find.
     * @return The user with the given email, or null if not found.
     */
    Optional<UserAccount> findByUserEmail(String userEmail);
}

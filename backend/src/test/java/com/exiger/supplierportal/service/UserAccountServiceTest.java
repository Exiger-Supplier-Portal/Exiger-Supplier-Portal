package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.UserAccountRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccountResponse;
import com.exiger.supplierportal.model.UserAccount;
import com.exiger.supplierportal.repository.UserAccountRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserAccountServiceTest {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setUp() {
        userAccountRepository.deleteAll();
    }

    @Test
    public void getAllUserEmails_WhenEmpty_returnsEmptyList() {
        List<String> emails = userAccountService.getAllUserEmails();
        assertNotNull(emails);
        assertTrue(emails.isEmpty());
    }

    @Test
    public void getAllUserEmails_returnsEmails() {
        UserAccount user1 = new UserAccount();
        user1.setUserEmail("user1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");

        UserAccount user2 = new UserAccount();
        user2.setUserEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        userAccountRepository.save(user1);
        userAccountRepository.save(user2);
        entityManager.flush();

        List<String> emails = userAccountService.getAllUserEmails();

        assertEquals(2, emails.size());
        assertTrue(emails.contains("user1@example.com"));
        assertTrue(emails.contains("user2@example.com"));
    }

    @Test
    public void createUser_WhenValid_ShoudReturnResponse() {
        UserAccountRequest request = new UserAccountRequest();
        request.setUserEmail("newuser@example.com");
        request.setFirstName("Alice");
        request.setLastName("Johnson");

        UserAccountResponse response = userAccountService.createUser(request);

        assertNotNull(response);
        assertEquals("newuser@example.com", response.getUserEmail());
        assertEquals("Alice", response.getFirstName());
        assertEquals("Johnson", response.getLastName());

        UserAccount persisted = userAccountRepository.findById(response.getUserEmail())
            .orElseThrow(() -> new AssertionError("User not persisted"));
        assertEquals("Alice", persisted.getFirstName());
        assertEquals("Johnson", persisted.getLastName());
    }

    @Test
    public void createUser_WhenDuplicateEmail_ShouldThrowException() {
        UserAccount existingUser = new UserAccount();
        existingUser.setUserEmail("duplicate@example.com");
        existingUser.setFirstName("Bob");
        existingUser.setLastName("Smith");
        userAccountRepository.save(existingUser);
        entityManager.flush();

        UserAccountRequest request = new UserAccountRequest();
        request.setUserEmail("duplicate@example.com");
        request.setFirstName("New");
        request.setLastName("User");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.createUser(request);
        });
        assertTrue(exception.getMessage().contains("User account already created with given email"));
    }
}

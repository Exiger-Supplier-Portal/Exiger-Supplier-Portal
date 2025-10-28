package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccessResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;
import com.exiger.supplierportal.model.Client;
import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.model.UserAccount;
import com.exiger.supplierportal.repository.ClientRepository;
import com.exiger.supplierportal.repository.ClientSupplierRepository;
import com.exiger.supplierportal.repository.UserAccessRepository;
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
public class UserAccessServiceTest {

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private UserAccessRepository userAccessRepository;

    @Autowired
    private ClientSupplierRepository clientSupplierRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EntityManager entityManager;

    private UserAccount testUser;
    private ClientSupplier testClientSupplier;

    @BeforeEach
    public void setUp() {
        userAccessRepository.deleteAll();
        clientSupplierRepository.deleteAll();
        userAccountRepository.deleteAll();

        testUser = new UserAccount();
        testUser.setUserEmail("user@test.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userAccountRepository.save(testUser);

        // Create a test client-supplier
        Client client = new Client();
        client.setClientId("client1");
        client.setClientName("Test Client");
        client.setClientEmail("client@test.com");
        entityManager.persist(client);

        testClientSupplier = new ClientSupplier();
        testClientSupplier.setClient(client);
        testClientSupplier.setSupplierId("supplier1");
        testClientSupplier.setSupplierName("Supplier Name");
        testClientSupplier.setStatus(SupplierStatus.APPROVED);
        clientSupplierRepository.save(testClientSupplier);

        entityManager.flush();
    }

    @Test
    public void createUserAccess_WhenValid_ShouldReturnAccess() {
        UserAccessRequest request = new UserAccessRequest();
        request.setClientSupplierId(testClientSupplier.getId());
        request.setUserEmail(testUser.getUserEmail());

        UserAccessResponse response = userAccessService.createUserAccess(request);

        assertNotNull(response.getId());
        assertEquals(testClientSupplier.getId(), response.getClientSupplierId());
        assertEquals(testUser.getUserEmail(), response.getUserEmail());

        assertTrue(userAccessRepository.existsById(response.getId()));
    }

    @Test
    public void createUserAccess_WhenExists_ShouldThrowException() {
        UserAccessRequest request = new UserAccessRequest();
        request.setClientSupplierId(testClientSupplier.getId());
        request.setUserEmail(testUser.getUserEmail());
        userAccessService.createUserAccess(request);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            userAccessService.createUserAccess(request)
        );
        assertTrue(exception.getMessage().contains("User access already exists"));
    }

    @Test
    public void createUserAccess_WhenRelationshipNotFound_ShouldThrowException() {
        UserAccessRequest request = new UserAccessRequest();
        request.setClientSupplierId(9999L);
        request.setUserEmail(testUser.getUserEmail());

        assertThrows(RelationshipNotFoundException.class, () ->
            userAccessService.createUserAccess(request)
        );
    }

    @Test
    public void createUserAccess_WhenUserNotFound_ShouldThrowException() {
        UserAccessRequest request = new UserAccessRequest();
        request.setClientSupplierId(testClientSupplier.getId());
        request.setUserEmail("nonexistent@test.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            userAccessService.createUserAccess(request)
        );
        assertTrue(exception.getMessage().contains("User account does not exist"));
    }

    @Test
    public void deleteUserAccess_WhenValid_ShouldReturnUser() {
        UserAccessRequest request = new UserAccessRequest();
        request.setClientSupplierId(testClientSupplier.getId());
        request.setUserEmail(testUser.getUserEmail());
        userAccessService.createUserAccess(request);

        UserAccessResponse response = userAccessService.deleteUserAccess(request);
        assertEquals(testClientSupplier.getId(), response.getClientSupplierId());
        assertEquals(testUser.getUserEmail(), response.getUserEmail());
        assertFalse(userAccessRepository.existsByUserAccount_UserEmailAndClientSupplier_Id(
            testUser.getUserEmail(), testClientSupplier.getId()));
    }

    @Test
    public void deleteUserAccess_WhenAccessNotFound_ShouldThrowException() {
        UserAccessRequest request = new UserAccessRequest();
        request.setClientSupplierId(testClientSupplier.getId());
        request.setUserEmail(testUser.getUserEmail());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            userAccessService.deleteUserAccess(request)
        );
        assertTrue(exception.getMessage().contains("User access does not exist"));
    }

    @Test
    public void getUserAccessListByUser_WhenValid_ShouldReturnList() {
        UserAccessRequest request1 = new UserAccessRequest();
        request1.setClientSupplierId(testClientSupplier.getId());
        request1.setUserEmail(testUser.getUserEmail());
        userAccessService.createUserAccess(request1);

        List<UserAccessResponse> accessList = userAccessService.getUserAccessListByUser(testUser.getUserEmail());
        assertEquals(1, accessList.size());
        assertEquals(testUser.getUserEmail(), accessList.get(0).getUserEmail());
    }

    @Test
    public void getUserAccessListByClientSupplierId_WhenValid_ShouldReturnList() {
        UserAccessRequest request1 = new UserAccessRequest();
        request1.setClientSupplierId(testClientSupplier.getId());
        request1.setUserEmail(testUser.getUserEmail());
        userAccessService.createUserAccess(request1);

        List<UserAccessResponse> accessList = userAccessService.getUserAccessListByClientSupplierId(testClientSupplier.getId());
        assertEquals(1, accessList.size());
        assertEquals(testClientSupplier.getId(), accessList.get(0).getClientSupplierId());
    }
}


package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.UserAccessRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.UserAccessResponse;
import com.exiger.supplierportal.exception.RelationshipNotFoundException;
import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.model.UserAccess;
import com.exiger.supplierportal.model.UserAccount;
import com.exiger.supplierportal.repository.ClientSupplierRepository;
import com.exiger.supplierportal.repository.UserAccessRepository;
import com.exiger.supplierportal.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing UserAccess entities.
 * Provides business logic for user access to relationships
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserAccessService {

    private final UserAccessRepository userAccessRepository;
    private final ClientSupplierRepository clientSupplierRepository;
    private final UserAccountRepository userAccountRepository;

    /**
     * Grant user access to a client-supplier relationship
     * @param request UserAccessRequest object
     * @return UserAccessResponse object
     */
    public UserAccessResponse createUserAccess(UserAccessRequest request) {
        // Check if access already exists
        if (userAccessRepository.existsByUserAccount_UserEmailAndClientSupplier_Id(
            request.getUserEmail(), request.getClientSupplierId()
        )) {
            throw new IllegalArgumentException(
                "User access already exists between client-supplier relationship" + request.getClientSupplierId() + "and user" + request.getUserEmail()
            );
        }

        // Fetch the ClientSupplier entity
        ClientSupplier relationship = clientSupplierRepository.findById(request.getClientSupplierId())
            .orElseThrow(() -> new RelationshipNotFoundException(request.getClientSupplierId()));

        // Fetch UserAccount entity
        UserAccount user = userAccountRepository.findByUserEmail(request.getUserEmail())
            .orElseThrow(() -> new IllegalArgumentException(
                "User account does not exist for given email" + request.getUserEmail()
        ));

        // Create new access using ORM
        UserAccess access = new UserAccess();
        access.setClientSupplier(relationship);
        access.setUserAccount(user);

        UserAccess savedAccess = userAccessRepository.save(access);
        return convertToResponse(savedAccess);
    }

    /**
     * Revoke a user's access to a client-supplier relationship
     * @param request UserAccessRequest object
     * @return UserAccessResponse object with the deleted access information
     */
    public UserAccessResponse deleteUserAccess(UserAccessRequest request) {
        // Check if access doesn't exist
        if (!userAccessRepository.existsByUserAccount_UserEmailAndClientSupplier_Id(
            request.getUserEmail(), request.getClientSupplierId()
        )) {
            throw new IllegalArgumentException(
                "User access does not exist between client-supplier relationship" + request.getClientSupplierId() + "and user" + request.getUserEmail()
            );
        }

        // Fetch the ClientSupplier entity
        ClientSupplier relationship = clientSupplierRepository.findById(request.getClientSupplierId())
            .orElseThrow(() -> new RelationshipNotFoundException(request.getClientSupplierId()));

        // Fetch UserAccount entity
        UserAccount user = userAccountRepository.findByUserEmail(request.getUserEmail())
            .orElseThrow(() -> new IllegalArgumentException(
                "User account does not exist for given email" + request.getUserEmail()
            ));

        // Delete user access using ORM
        UserAccess access = new UserAccess();
        access.setClientSupplier(relationship);
        access.setUserAccount(user);

        userAccessRepository.delete(access);
        return convertToResponse(access);
    }

    /**
     * Get UserAccess entities that userEmail has access to
     * @param userEmail email of user
     * @return List of UserAccessResponse
     */
    public List<UserAccessResponse> getUserAccessListByUser(String userEmail) {
        List<UserAccess> accessList = userAccessRepository.findAllByUserAccount_UserEmail(userEmail);
        return accessList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get UserAccess entities that clientSupplierId belongs to
     * @param clientSupplierId ID of ClientSupplier entity
     * @return List of UserAccessResponse
     */
    public List<UserAccessResponse> getUserAccessListByClientSupplierId(Long clientSupplierId) {
        List<UserAccess> accessList = userAccessRepository.findAllByClientSupplier_Id(clientSupplierId);
        return accessList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Convert UserAccess entity to UserAccessResponse DTO
     */
    private UserAccessResponse convertToResponse(UserAccess access) {
        UserAccessResponse response = new UserAccessResponse();
        response.setId(access.getId());
        response.setClientSupplierId(access.getClientSupplier().getId());
        response.setUserEmail(access.getUserAccount().getUserEmail());
        return response;
    }
}

package com.exiger.supplierportal.repository;

import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.model.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * Repository for managing user accounts
 */
@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

    /**
     * Get all UserAccess objects that userEmail has access to
     *
     * @param userEmail email of user account
     * @return List of UserAccess objects that associate to userEmail
     */
    List<UserAccess> findAllByUserAccount_UserEmail(String userEmail);

    /**
     * Get all UserAccess objects that include clientSupplierId
     *
     * @param clientSupplierId ID of ClientSupplier object
     * @return List of UserAccess objects that have access to ClientSupplier object
     */
    List<UserAccess> findAllByClientSupplier_Id(Long clientSupplierId);

    /**
     * Check if userEmail has access to clientSupplierId
     * @param userEmail email of user account
     * @param clientSupplierId ID of ClientSupplier object
     * @return true if user has access, false otherwise
     */
    boolean existsByUserAccount_UserEmailAndClientSupplier_Id(String userEmail, Long clientSupplierId);

    /**
     * Get UserAccess object between userEmail and clientSupplierId, if it exists
     * @param userEmail email of user account
     * @param clientSupplierId ID of ClientSupplier object
     * @return UserAccess if it exists, null otherwise
     */
    Optional<UserAccess> findByUserAccount_UserEmailAndClientSupplier_Id(String userEmail, Long clientSupplierId);

    /**
     * Get all ClientSupplier objects by userEmail.
     *
     * @param userEmail email of user account
     * @return List of ClientSupplier objects
     */
    @Query("SELECT access.clientSupplier FROM UserAccess access WHERE access.userAccount.userEmail = :userEmail")
    List<ClientSupplier> findAllClientSuppliersByUserEmail(@Param("userEmail") String userEmail);

    /**
     * Get ClientSupplier object by userEmail and clientId, if exists
     *
     * @param userEmail email of user account
     * @param clientId ID of client
     * @return Optional ClientSupplier
     */
    @Query("""
    SELECT ua.clientSupplier
    FROM UserAccess ua
    WHERE ua.userAccount.userEmail = :userEmail
      AND ua.clientSupplier.client.clientId = :clientId""")
    Optional<ClientSupplier> findClientSupplierByUserEmailAndClientId(
        @Param("userEmail") String userEmail,
        @Param("clientId") String clientId);
}

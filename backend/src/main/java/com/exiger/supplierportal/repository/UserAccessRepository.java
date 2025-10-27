package com.exiger.supplierportal.repository;

import com.exiger.supplierportal.model.ClientSupplier;
import com.exiger.supplierportal.model.UserAccess;
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
public interface UserAccessRepository extends JpaRepository<UserAccess, String> {

    /**
     * Get all ClientSupplier objects that userEmail has access to
     *
     * @param userEmail email of user account
     * @return List of ClientSupplier objects that associate to userEmail
     */
    List<ClientSupplier> findAllClientSupplierByUserAccount_UserEmail(String userEmail);

    /**
     * Get all UserAccount objects that have access to clientSupplierId
     *
     * @param clientSupplierId ID of ClientSupplier object
     * @return List of UserAccount objects that have access to ClientSupplier object
     */
    List<UserAccount> findAllUserAccountByClientSupplier_Id(Long clientSupplierId);

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
}

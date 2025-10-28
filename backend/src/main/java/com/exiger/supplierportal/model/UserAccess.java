package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a client-supplier relationship that a user has access to
 * Primary key is id
 * The client_supplier_id references a client-supplier relationship
 * The user_email references a user
 * UniqueConstraint on client_supplier_id and user_email to ensure no duplicate access for a user
 */
@Entity
@Data
@Table(name = "user_access", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"client_supplier_id", "user_email"})
})
public class UserAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_supplier_id", nullable = false)
    private ClientSupplier clientSupplier;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_email", referencedColumnName = "user_email", nullable = false)
    private UserAccount userAccount;
}

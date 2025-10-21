package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a pending supplier registration, before they have an account
 * Primary key is ID
 * supplierEmail represents the suppliers point of contact during the registration process
 * token represents the unique identifier for creating a temporary registration link
 * expiration represents the date and time the token becomes invalid and the supplier can no longer register
 */
@Entity
@Data
@Table(name = "supplier_registration")
public class SupplierRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "supplier_email", nullable = false)
    private String supplierEmail;

    @Column(name = "token", nullable = false, unique = true)
    private UUID token;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;
}

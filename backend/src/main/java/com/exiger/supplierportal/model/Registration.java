package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a pending registration for a user account
 * Primary key is id
 * clientID is a foreign key referencing Client
 * supplierID refers to a supplier
 * inviteEmail refers to the email the registration invite link is sent to
 * token represents the unique identifier for creating a temporary registration link
 * expiration represents the date and time the token becomes invalid and the user can no longer register
 */
@Entity
@Data
@Table(name = "registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @Column(name = "invite_email", nullable = false)
    private String inviteEmail;

    @Column(name = "token", nullable = false, unique = true)
    private UUID token;

    @Column(name = "expiration", nullable = false)
    private Instant expiration;
}

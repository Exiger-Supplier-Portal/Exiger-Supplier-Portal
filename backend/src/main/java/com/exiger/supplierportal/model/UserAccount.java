package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents an account for a user to log into the supplier portal
 * Primary key is userEmail
 * Attributes for first and last name
 */
@Entity
@Data
@Table(name = "user_account")
public class UserAccount {

    @Id
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;
}

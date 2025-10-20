package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Represents a client.
 * Primary key is clientID.
 * clientName and clientEmail represents the client's company name and contact email, respectively.
 */
@Entity
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_id")
    private String clientID;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_email", unique = true, nullable = false)
    private String clientEmail;
}
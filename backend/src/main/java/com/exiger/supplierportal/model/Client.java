package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Represents a client.
 * Primary key is id.
 * The name and email represents the client's company name and contact email, respectively.
 */
@Entity
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;
}

package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a supplier.
 * Primary key is id.
 * The name and email represents the supplier's company name and contact email, respectively.
 */
@Entity
@Data
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;
}

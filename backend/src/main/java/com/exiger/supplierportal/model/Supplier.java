package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a supplier.
 * Primary key is supplierID.
 * supplierName and supplierEmail represents the supplier's company name and contact email, respectively.
 */
@Entity
@Data
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierID;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "supplier_email", unique = true, nullable = false)
    private String supplierEmail;
}

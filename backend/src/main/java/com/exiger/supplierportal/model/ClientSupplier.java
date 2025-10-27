package com.exiger.supplierportal.model;

import com.exiger.supplierportal.enums.SupplierStatus;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a relationship between supplier and client.
 * Primary key is id
 * Foreign key client_id references Client
 * The supplierId is manually set.
 * The supplierStatus indicates the current state of the supplier in the
 * relationship.
 * The supplierName is the name of the supplier company
 * UniqueConstraint on client_id and supplier_id ensures that no two rows
 * contain the same combination of the attributes
 */
@Entity
@Data
@Table(name = "client_supplier", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"client_id", "supplier_id"})
})
public class ClientSupplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @Enumerated(EnumType.STRING)
    @Column(name = "supplier_status", nullable = false)
    private SupplierStatus status;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;
}

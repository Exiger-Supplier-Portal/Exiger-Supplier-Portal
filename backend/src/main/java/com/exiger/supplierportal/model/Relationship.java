package com.exiger.supplierportal.model;

import com.exiger.supplierportal.enums.SupplierStatus;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a relationship between supplier and client.
 * Primary key is composite and consists of the supplierID and clientID.
 * The supplierStatus indicates the current state of the supplier in the
 * relationship.
 */

@Entity
@Data
@Table(name = "relationship")
public class Relationship {
    @EmbeddedId
    private RelationshipID id;

    @ManyToOne
    @MapsId("clientID")
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @MapsId("supplierID")
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "supplier_status", nullable = false)
    private SupplierStatus status;
}

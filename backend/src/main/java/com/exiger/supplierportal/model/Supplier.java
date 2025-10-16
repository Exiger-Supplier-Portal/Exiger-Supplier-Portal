package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


/**
 * Represents a supplier in the system.
 * 
 * Each supplier has a unique ID and a name, and can be linked to multiple clients
 * through a Relationship. The relationships list stores all connections
 * between this supplier and various clients, along with their statuses.
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


}
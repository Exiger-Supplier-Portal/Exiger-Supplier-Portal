package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * Represents a client in the system.
 * 
 * Each client has a unique ID and a name, and can be linked to multiple suppliers
 * through a Relationship. The relationships list stores all connections
 * between this supplier and various clients, along with their statuses.
 */
@Entity
@Data
@Table(name = "client")
public class Client {

  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "client_id")
  private Long clientID;

  @Column(name = "client_name", nullable = false)
  private String clientName;


}
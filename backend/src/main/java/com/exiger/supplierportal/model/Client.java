package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

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

  @OneToMany(mappedBy = "id.clientID", cascade = CascadeType.ALL)
  private List<Relationship> relationships;

}
// package com.exiger.supplierportal.service;

// import com.exiger.supplierportal.model.Relationship;
// import com.exiger.supplierportal.repository.RelationshipRepository; // changes to what James writes for repository
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class RelationshipService {
//   private final RelationshipRepository repository;

//   public RelationshipService(RelationshipRepository repository) {
//     this.repository = repository;
//   }

//   public List<Long> getClientIdsForSupplier(Long supplierId) {
//     List<Relationship> relationships = repository.findByIdSupplierID(supplierId); // change to method James writes
        
//     if (relationships.isEmpty()) {
//       throw new RuntimeException("No relationships found for supplier ID: " + supplierId);
//     }
        
//     return relationships.stream()
//             .map(relationship -> relationship.getId().getClientID())
//             .collect(Collectors.toList());
//     }
// }
package com.exiger.supplierportal.exception;

/**
 * Custom exception for when attempting to access a relationship that does not exist.
 * Thrown when a relationship is needed for an action but does not exist.
 *    Ex: Trying to update a relationship for a relationship that does not exist
 */
public class RelationshipNotFoundException extends RuntimeException {
    
    public RelationshipNotFoundException(String clientId, String supplierId) {
      super("ClientSupplier not found between client " + clientId + " and supplier " + supplierId);
    }

    public RelationshipNotFoundException(Long id) {
        super("ClientSupplier not found for id " + id);
    }
}
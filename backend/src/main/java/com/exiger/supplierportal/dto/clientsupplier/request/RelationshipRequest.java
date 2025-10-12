package com.exiger.supplierportal.dto.clientsupplier.request;

import com.exiger.supplierportal.enums.SupplierStatus;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for receiving data to create/update a Client-Supplier relationship.
 * Requires the ids for the client and supplier and the desired new status.
 */
@Data
public class RelationshipRequest {
    @NotNull
    private Long clientID;

    @NotNull
    private Long supplierID;

    @NotNull
    private SupplierStatus status;
}

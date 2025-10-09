package com.exiger.supplierportal.dto.clientsupplier.response;

import com.exiger.supplierportal.enums.SupplierStatus;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for sending client and supplier relationship data
 * Sends the ids for the client and supplier and the status of their relationship.
 */
@Data
public class RelationshipResponse {
    @NotNull
    private Long clientID;

    @NotNull
    private Long supplierID;

    @NotNull
    private SupplierStatus status;
}

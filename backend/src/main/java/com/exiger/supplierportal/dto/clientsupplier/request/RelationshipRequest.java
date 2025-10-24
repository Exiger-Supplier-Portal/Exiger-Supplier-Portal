package com.exiger.supplierportal.dto.clientsupplier.request;

import com.exiger.supplierportal.enums.SupplierStatus;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for receiving data to create/update a Client-Supplier relationship.
 * Requires the ids for the client and supplier and the desired new status.
 */
@Schema(description = "Request for recieving data to create/update a Client-Supplier relationship")
@Data
public class RelationshipRequest {
    @Schema(description = "Unique identifier for the client", example = "[client-id]")
    @NotNull
    private String clientID;

    @Schema(description = "Unique identifier for the supplier", example = "[supplier-id]")
    @NotNull
    private String supplierID;

    @Schema(description = "Status of the Client-Supplier relationship", example = "INVITED")
    @NotNull
    private SupplierStatus status;
}

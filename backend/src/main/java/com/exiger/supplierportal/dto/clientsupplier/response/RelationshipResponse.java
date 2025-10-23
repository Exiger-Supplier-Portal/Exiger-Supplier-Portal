package com.exiger.supplierportal.dto.clientsupplier.response;

import com.exiger.supplierportal.enums.SupplierStatus;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending client and supplier relationship data
 * Sends the ids for the client and supplier and the status of their relationship.
 */
@Schema(description = "Response object containing client and supplier relationship data")
@Data
public class RelationshipResponse {
    @Schema(description = "Unique identifer for the client", example = "[client-id]")
    @NotNull
    private String clientID;

    @Schema(description = "Unique identifer for the supplier", example = "[supplier-id]")
    @NotNull
    private String supplierID;

    @Schema(description = "Status of the Client-Supplier relationship", example = "ONBOARDING")
    @NotNull
    private SupplierStatus status;
}

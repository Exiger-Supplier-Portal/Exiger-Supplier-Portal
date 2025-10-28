package com.exiger.supplierportal.dto.clientsupplier.response;

import com.exiger.supplierportal.enums.SupplierStatus;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending client and supplier relationship data
 * Sends the ids for the client and supplier and the status of their relationship.
 */
@Schema(description = "Response object containing client and supplier relationship data")
@Data
public class ClientSupplierResponse {
    @Schema(description = "ID of the relationship")
    private Long id;

    @Schema(description = "Unique identifer for the client", example = "[client-id]")
    private String clientId;

    @Schema(description = "Unique identifer for the supplier", example = "[supplier-id]")
    private String supplierId;

    @Schema(description = "Status of the Client-Supplier relationship", example = "ONBOARDING")
    private SupplierStatus status;

    @Schema(description = "Name of supplier company", example = "Home Depot")
    private String supplierName;
}

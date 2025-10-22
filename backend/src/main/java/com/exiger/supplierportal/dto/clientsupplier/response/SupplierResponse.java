package com.exiger.supplierportal.dto.clientsupplier.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending supplier data
 * Sends the ids for the client and supplier and the status of their relationship.
 */
@Schema(description = "Response for sending supplier data")
@Data
public class SupplierResponse {
    @Schema(description = "Unique identifier for the supplier", example = "[supplier-id]")
    @NotBlank
    private String supplierID;

    @Schema(description = "Name of the supplier", example = "Test Supplier")
    @NotBlank
    private String supplierName;

    @Schema(description = "Email of the supplier", example = "test@supplier.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String supplierEmail;
}

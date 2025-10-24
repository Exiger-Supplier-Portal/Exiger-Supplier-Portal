package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for receiving data to create a supplier.
 * Requires supplierName and supplierEmail.
 */

@Schema(description = "Request for receiving data to create a supplier")
@Data
public class SupplierRequest {
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

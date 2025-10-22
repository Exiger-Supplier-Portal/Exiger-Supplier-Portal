package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for receiving data to create a SupplierRegistration
 * Requires clientID and supplierEmail
 * ID, token, and expiration are all generation server-side
 */
@Schema(description = "Request for receiving data to create a SupplierRegistration")
@Data
public class SupplierRegistrationRequest {
    @Schema(description = "Unique identifier for the client", example = "[client-id]")
    @NotBlank
    private String clientID;

    @Schema(description = "Email address of the supplier", example = "test@supplier.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String supplierEmail;
}

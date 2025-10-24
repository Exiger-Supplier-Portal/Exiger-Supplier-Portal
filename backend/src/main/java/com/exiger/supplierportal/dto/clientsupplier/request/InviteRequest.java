package com.exiger.supplierportal.dto.clientsupplier.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for receiving data needed to invite a new suppplier for a specific client.
 * Requires 1Exiger's customer id for the client and a supplier email.
 */
@Schema(description = "Request object containing data to create an invite link for a supplier")
@Data
public class InviteRequest {
    @Schema(description = "Unique identifier for the client", example = "[client-id]")
    @NotBlank(message = "Client ID is required")
    private String clientId;

    @Schema(description = "Email of the supplier", example = "test@supplier.com")
    @NotBlank(message = "Supplier email is required")
    @Email(message = "Invalid email format")
    private String supplierEmail;
}

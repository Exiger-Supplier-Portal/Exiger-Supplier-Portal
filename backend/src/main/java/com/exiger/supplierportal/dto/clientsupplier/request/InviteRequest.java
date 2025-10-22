package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for receiving data needed to invite a new suppplier for a specific client.
 * Requires 1Exiger's customer id for the client and a supplier email.
 */
@Data
public class InviteRequest {
    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Supplier email is required")
    @Email(message = "Invalid email format")
    private String supplierEmail;
}

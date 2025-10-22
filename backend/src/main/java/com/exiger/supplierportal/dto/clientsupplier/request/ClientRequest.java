package com.exiger.supplierportal.dto.clientsupplier.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for receiving data to create a new client.
 * Requires the id, name, and email of the client.
 */
@Data
public class ClientRequest {
    @NotBlank
    private String clientID;

    @NotBlank
    private String clientName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String clientEmail;
}

package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for sending data to create a new client.
 * Sends the id, name, and email of the client.
 */
@Data
public class ClientResponse {
    @NotBlank
    private String clientID;

    @NotBlank
    private String clientName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String clientEmail;
}

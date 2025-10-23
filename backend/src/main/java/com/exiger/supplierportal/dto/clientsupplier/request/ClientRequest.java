package com.exiger.supplierportal.dto.clientsupplier.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for receiving data to create a new client.
 * Requires the id, name, and email of the client.
 */
@Schema(description = "Request object for creating a new client")
@Data
public class ClientRequest {
    @Schema(description = "Unique identifier for the client", example = "[client-id]")
    @NotBlank
    private String clientID;

    @Schema(description = "Name of the client company", example = "Test Client")
    @NotBlank
    private String clientName;

    @Schema(description = "Email address of the client", example = "test@client.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String clientEmail;
}

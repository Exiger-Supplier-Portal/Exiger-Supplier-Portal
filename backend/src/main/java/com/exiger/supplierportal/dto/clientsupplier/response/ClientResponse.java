package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending data to create a new client.
 * Sends the id, name, and email of the client.
 */
@Schema(description = "Response object containing created client data")
@Data
public class ClientResponse {
    @Schema(description = "Unique identifier for the client", example = "[client-id]")
    private String clientID;

    @Schema(description = "Name of the client company", example = "Test Client")
    private String clientName;

    @Schema(description = "Email address of the client", example = "test@client.com")
    private String clientEmail;
}

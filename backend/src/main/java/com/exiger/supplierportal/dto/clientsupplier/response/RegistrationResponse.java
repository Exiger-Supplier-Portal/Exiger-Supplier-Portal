package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending registration result data from POST /api/register.
 * Sends success status, message, and supplier ID after registration.
 */
@Schema(description = "Response object containing registration result data from Post /api/register")
@Data
public class RegistrationResponse {
    @Schema(description = "Success status of the registration", example = "TRUE")
    private boolean success;

    @Schema(description = "Registration message", example = "Registration successful")
    private String message;

    @Schema(description = "Unique identifier for the supplier", example = "[supplier-id]")
    private String supplierId;
}

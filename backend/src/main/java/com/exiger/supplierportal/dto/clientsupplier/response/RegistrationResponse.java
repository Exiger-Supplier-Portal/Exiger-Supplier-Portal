package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;

/**
 * DTO for sending registration result data from POST /api/register.
 * Sends success status, message, and supplier ID after registration.
 */
@Data
public class RegistrationResponse {
    private boolean success;
    private String message;
    private String supplierId;
}

package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for receiving data to create a SupplierRegistration
 * Requires clientID and supplierEmail
 * ID, token, and expiration are all generation server-side
 */
@Data
public class SupplierRegistrationRequest {
    @NotBlank
    private String clientID;

    @NotBlank
    @Email(message = "Email should be valid")
    private String supplierEmail;
}

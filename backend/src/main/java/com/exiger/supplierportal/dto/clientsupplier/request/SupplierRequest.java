package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for receiving data to create a supplier.
 * Requires supplierName and supplierEmail.
 */
@Data
public class SupplierRequest {
    @NotBlank
    private String supplierID;

    @NotBlank
    private String supplierName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String supplierEmail;
}

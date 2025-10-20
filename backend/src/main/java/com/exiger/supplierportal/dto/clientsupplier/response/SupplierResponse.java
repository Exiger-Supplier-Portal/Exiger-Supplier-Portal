package com.exiger.supplierportal.dto.clientsupplier.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for sending supplier data
 * Sends the ids for the client and supplier and the status of their relationship.
 */
@Data
public class SupplierResponse {
    @NotBlank
    private String supplierID;

    @NotBlank
    private String supplierName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String supplierEmail;
}

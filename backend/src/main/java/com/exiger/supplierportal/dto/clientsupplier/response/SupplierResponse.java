package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for sending supplier data
 * Sends the ids for the client and supplier and the status of their relationship.
 */
@Data
public class SupplierResponse {
    @NotNull
    private String supplierID;

    @NotNull
    private String supplierName;

    @NotNull
    private String supplierEmail;
}

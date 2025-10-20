package com.exiger.supplierportal.dto.clientsupplier.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for receiving data to create a supplier.
 * Requires supplierName and supplierEmail.
 */
@Data
public class SupplierRequest {
    @NotNull
    private String supplierName;

    @NotNull
    private String supplierEmail;
}

package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for receiving registration form data to create a supplier account.
 * Requires email and supplier name from the registration form.
 * Password is handled by Okta through activation email - not collected by our API.
 */
@Schema(description = "Request for receiving registration form data to create a supplier account")
@Data
public class RegistrationRequest {
    // TODO: update to reflect new registration flow
//    @Schema(description = "Unique email for the supplier", example = "test@supplier.com")
//    @NotBlank
//    @Email(message = "Email should be valid")
//    private String email;
//
//    @Schema(description = "Name of the supplier", example = "Test Supplier")
//    @NotBlank
//    @Size(min = 2, max = 100, message = "Supplier name must be between 2 and 100 characters")
//    private String supplierName;
}

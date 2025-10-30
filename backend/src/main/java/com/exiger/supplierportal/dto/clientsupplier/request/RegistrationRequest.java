package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request body for supplier registration.
 * Form collects first and last name; email is derived from the invite.
 */
@Schema(description = "Supplier registration input containing first and last name")
@Data
public class RegistrationRequest {

    @Schema(description = "User first name", example = "Jane")
    @NotBlank
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;

    @Schema(description = "User last name", example = "Doe")
    @NotBlank
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;
}

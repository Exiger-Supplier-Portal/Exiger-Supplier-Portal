package com.exiger.supplierportal.dto.clientsupplier.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for receiving data to create a new user account.
 */
@Schema(description = "Request object for creating a new user account")
@Data
public class UserAccountRequest {
    @Schema(description = "Email address of the user", example = "test@client.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String userEmail;

    @Schema(description = "First name of user", example = "John")
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of user", example = "Doe")
    @NotBlank
    private String lastName;
}

package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending data to grant a user access to a new client.
 */
@Schema(description = "Request object for creating a new user account")
@Data
public class UserAccessRequest {
    @Schema(description = "ID of client-supplier relationship")
    @NotNull
    private Long clientSupplierId;

    @Schema(description = "Email address of the user", example = "test@client.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String userEmail;
}

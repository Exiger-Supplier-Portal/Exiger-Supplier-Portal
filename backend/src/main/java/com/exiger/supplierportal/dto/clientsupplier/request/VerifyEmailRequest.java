package com.exiger.supplierportal.dto.clientsupplier.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for verifying a user's email from an invite link.
 */
@Schema(description = "Request object containing email of user that was invited")
@Data
public class VerifyEmailRequest {
    @Schema(description = "Email address of the user", example = "test@client.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String userEmail;
}

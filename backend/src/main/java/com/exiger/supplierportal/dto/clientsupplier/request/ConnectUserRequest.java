package com.exiger.supplierportal.dto.clientsupplier.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for connecting an existing authenticated user
 * to a client-supplier relationship using the registration token.
 */
@Data
@Schema(description = "Request DTO to connect an authenticated user to a client-supplier relationship.")
public class ConnectUserRequest {

    @Schema(description = "The registration token originally provided in the invite link.", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotBlank(message = "Registration token is required.")
    private String registrationToken;
}

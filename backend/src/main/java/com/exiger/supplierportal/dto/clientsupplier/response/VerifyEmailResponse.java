package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for verifying if a user's email exists already.
 * 
 * Returns true if it exsists and false if not
 */
@Schema(description = "Indicates whether the user's email already exists. Redirect occurs if email exists.")
@Data
public class VerifyEmailResponse {
    @Schema(description = "False if the user is new (email not found). No response is sent if the email exists because a redirect occurs.", example = "false")
    private Boolean emailExists;

    @Schema(description = "Registration token from URL", example = "550e8400-e29b-41d4-a716-446655440000")
    private String token;
}

package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for verifying if a user's email is new.
 * 
 * Only returned when the email does not exist in the system.
 * If the email already exists, the backend redirects to Okta for authentication instead.
 */
@Schema(description = "Indicates if the user email is new. Redirect occurs if email exists.")
@Data
public class VerifyEmailResponse {
    @Schema(description = "True if the user is new (email not found). No response is sent if the email exists because a redirect occurs.", example = "true")
    private Boolean newUser;
}

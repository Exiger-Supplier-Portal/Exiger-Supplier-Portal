package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for verifying if a user's email exists already.
 * 
 * Only returned when the email does not exist in the system.
 * If the email already exists, the backend redirects to Okta for authentication instead.
 */
@Schema(description = "Indicates whether the user's email already exists. Redirect occurs if email exists.")
@Data
public class VerifyEmailResponse {
    @Schema(description = "False if the user is new (email not found). No response is sent if the email exists because a redirect occurs.", example = "false")
    private Boolean emailExists;
}

package com.exiger.supplierportal.dto.clientsupplier.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

/**
 * DTO for returning a unique registration link and when it expires. 
 */
@Schema(description = "Response object containing invite response data from POST /api/invite")
@Data
public class InviteResponse {
    @Schema(description = "One-time unique link for the supplier to click on to register. Expires in 24 hours after creation", example = "https://supplierportal.exiger.com/api/register?token=abcdefg")
    private String registrationUrl;

    @Schema(description = "Expiration date of registration link", example = "1970-01-01T00:00:00Z")
    private Instant expiresAt;
}

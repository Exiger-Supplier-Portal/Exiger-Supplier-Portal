package com.exiger.supplierportal.dto.clientsupplier.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending supplier registration data
 */
@Schema(description = "Response object containing supplier registration data")
public class SupplierRegistrationResponse {
    @Schema(description = "Unique identifier for the supplier registration", example = "[id]")
    private Long id;

    @Schema(description = "Unique identifier for the client", example = "[client-id]")
    private String clientID;

    @Schema(description = "Email address of the supplier", example = "test@supplier.com")
    private String supplierEmail;

    @Schema(description = "Unique token for supplier registration", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID token;

    @Schema(description = "Expiration date of the supplier registration", example = "2025-12-31 11:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiration;
}

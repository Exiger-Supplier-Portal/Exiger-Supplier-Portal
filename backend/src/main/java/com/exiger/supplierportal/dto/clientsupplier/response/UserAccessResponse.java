package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for sending data to grant a user access to a new client.
 */
@Schema(description = "Response object for retrieving user access information")
@Data
public class UserAccessResponse {
    @Schema(description = "ID of the UserAccess object")
    private Long id;

    @Schema(description = "ID of client-supplier relationship")
    private Long clientSupplierId;

    @Schema(description = "Email address of the user", example = "test@client.com")
    private String userEmail;
}

package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for verifying if connecting a supplier account to a new client-supplier relationship was successful
 * 
 * 
 * If the email already exists, the backend redirects to Okta for authentication instead.
 */
@Schema(description = "Indicates whether connecting new relationship was successful")
@Data
public class ConnectUserResponse {
    @Schema(description = "false if connection can not be added, and true if it is added", example = "false")
    private Boolean connectionSuccess;
}

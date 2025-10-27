package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for retrieving a user account.
 */
@Schema(description = "Response object for retrieving a new user account")
@Data
public class UserAccountResponse {
    @Schema(description = "Email address of the user", example = "test@client.com")
    private String userEmail;

    @Schema(description = "First name of user", example = "John")
    private String firstName;

    @Schema(description = "Last name of user", example = "Doe")
    private String lastName;
}

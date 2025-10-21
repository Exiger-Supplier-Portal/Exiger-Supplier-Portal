package com.exiger.supplierportal.dto.clientsupplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for receiving registration form data to create a supplier account.
 * Requires email and company name from the registration form.
 * Password is handled by Okta through activation email - not collected by our API.
 */
@Data
public class RegistrationRequest {
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;
}

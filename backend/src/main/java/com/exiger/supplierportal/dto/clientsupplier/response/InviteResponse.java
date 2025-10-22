package com.exiger.supplierportal.dto.clientsupplier.response;

import lombok.Data;
import java.time.Instant;

/**
 * DTO for returning a unique registration link and when it expires. 
 */
@Data
public class InviteResponse {
    private String registrationUrl;
    private Instant expiresAt;
}

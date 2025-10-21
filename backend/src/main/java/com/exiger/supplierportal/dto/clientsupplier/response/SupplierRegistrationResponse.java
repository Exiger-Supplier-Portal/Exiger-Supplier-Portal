package com.exiger.supplierportal.dto.clientsupplier.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for sending supplier registration data
 */
public class SupplierRegistrationResponse {
    private Long id;

    private String clientID;

    private String supplierEmail;

    private UUID token;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiration;
}

package com.exiger.supplierportal.dto.clientsupplier.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Map;

@Schema(description = "Standard error response returned by the API")
@Data
public class ApiErrorResponse {
    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Description of the error", example = "Relationship not found")
    private String message;

    @Schema(description = "Optional map of errors by field", nullable = true, example = "{\"username\": \"username already exists\", \"password\": \"must be at least 12 characters\"}")
    private Map<String, String> errors;
}

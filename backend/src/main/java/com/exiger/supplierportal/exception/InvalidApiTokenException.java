package com.exiger.supplierportal.exception;

/**
 * Custom exception for API token validation failures.
 * Thrown when API token is missing, invalid format, or doesn't match expected value.
 */
public class InvalidApiTokenException extends SecurityException {
    
    public InvalidApiTokenException(String message) {
        super(message);
    }
    
    public InvalidApiTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.exiger.supplierportal.exception;

/**
 * Custom exception for supplier registration failures.
 * Thrown when registration token is invalid, expired, or other registration-related errors occur.
 */
public class RegistrationException extends RuntimeException {
    
    public RegistrationException(String message) {
        super(message);
    }
    
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

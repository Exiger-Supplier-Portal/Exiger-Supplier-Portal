package com.exiger.supplierportal.enums;

/**
 * Represents the different states a supplier can be in.
 * INVITED: client has invited supplier to register, but hasn't done so yet.
 * ONBOARDING: supplier has registered and begun the onboarding process.
 * APPROVED: supplier has completed the onboarding process and is approved to do business with client.
 */
public enum SupplierStatus {
    INVITED, ONBOARDING, APPROVED
}
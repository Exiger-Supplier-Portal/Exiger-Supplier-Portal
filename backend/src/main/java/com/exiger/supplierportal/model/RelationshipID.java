package com.exiger.supplierportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite attribute of supplierID and clientID to serve as the id in Relationship
 */
@Embeddable
@Data
public class RelationshipID implements Serializable {
    @Column(name = "supplier_id")
    private Long supplierID;

    @Column(name = "client_id")
    private Long clientID;

    /**
     * Override equals() for RelationshipID
     * @param o   the reference object with which to compare.
     * @return True if supplierID and clientID of both objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RelationshipID)) {
            return false;
        }

        RelationshipID otherID = (RelationshipID) o;
        return supplierID.equals(otherID.supplierID) &&
            clientID.equals(otherID.clientID);
    }

    /**
     * Override hashCode() for RelationshipID
     * @return hash of supplierID and clientID
     */
    @Override
    public int hashCode() {
        return Objects.hash(supplierID, clientID);
    }
}

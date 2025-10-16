package com.exiger.supplierportal.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

/** 
 * Composite primary key for the Relationship entity.
 * 
 * This class is in a separate file because: 
 *  - Java requires public classes to be in their own file
 *  - Other packages (repository, service) need access to this class
 * 
 * Contains the clientId and supplierId that together form the unique identifier for each client-supplier relationship.
 */
@Embeddable
@Data
public class RelationshipID implements Serializable {
    private Long supplierID;
    private Long clientID;
}

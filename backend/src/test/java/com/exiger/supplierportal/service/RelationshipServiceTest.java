package com.exiger.supplierportal.service;

import com.exiger.supplierportal.dto.clientsupplier.request.RelationshipRequest;
import com.exiger.supplierportal.dto.clientsupplier.response.RelationshipResponse;
import com.exiger.supplierportal.enums.SupplierStatus;
import com.exiger.supplierportal.repository.RelationshipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RelationshipService.class)
@ActiveProfiles("test")
@Transactional 
class RelationshipServiceTest {

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Test
    void testCreateRelationship() {
        RelationshipRequest request = new RelationshipRequest();

        /**
         * Setup test data for the relationship
         */
        request.setClientID(1L);
        request.setSupplierID(2L);
        request.setStatus(SupplierStatus.INVITED);

        RelationshipResponse response = relationshipService.createRelationship(request);
        
        assertThat(response).isNotNull();
        assertThat(response.getClientID()).isEqualTo(1L);
        assertThat(response.getSupplierID()).isEqualTo(2L);
        assertThat(response.getStatus()).isEqualTo(SupplierStatus.INVITED);
    }
    
}
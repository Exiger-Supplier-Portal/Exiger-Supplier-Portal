package com.exiger.supplierportal.service;

import com.exiger.supplierportal.repository.SupplierRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(RelationshipService.class)
@ActiveProfiles("test")
@Transactional
public class SupplierRegistrationTest {

    @Autowired
    private SupplierRegistrationService supplierRegistrationService;

    @Autowired
    private SupplierRegistrationRepository supplierRegistrationRepository;

    @Autowired
    private TestEntityManager entityManager;
}

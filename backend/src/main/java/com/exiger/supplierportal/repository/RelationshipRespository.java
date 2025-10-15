package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.Relationship;

@Repository
public interface RelationshipRespository extends JpaRepository<Relationship, Long> {

    //Will add custom query methods here
}
package com.exiger.supplierportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.exiger.supplierportal.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    //Will add custom query methods here
}

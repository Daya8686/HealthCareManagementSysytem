package com.hcms.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcms.project.entity.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

}

package com.manuel.blogrestapi.repository;

import com.manuel.blogrestapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
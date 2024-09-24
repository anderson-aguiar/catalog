package com.anderson.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.catalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}

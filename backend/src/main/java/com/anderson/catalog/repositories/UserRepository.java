package com.anderson.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.catalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}

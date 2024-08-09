package com.anderson.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.catalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

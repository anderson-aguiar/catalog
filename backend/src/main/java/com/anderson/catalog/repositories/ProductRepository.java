package com.anderson.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.catalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

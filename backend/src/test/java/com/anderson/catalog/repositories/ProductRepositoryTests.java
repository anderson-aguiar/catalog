package com.anderson.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.anderson.catalog.entities.Product;
import com.anderson.catalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long exintingId;
	private long notExintingId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		notExintingId = 1000L;
		countTotalProducts = 25L;
	}
	@Test
	public void saveShoudPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {

		repository.deleteById(exintingId);
		Optional<Product> result = repository.findById(exintingId);
		Assertions.assertFalse(result.isPresent());

	}
	@Test
	public void findByIdShouldReturnNonEmptyOptinalWhenIdExist() {
		
		Optional<Product> result = repository.findById(exintingId);
		Assertions.assertTrue(result.isPresent());
		
	}
	@Test
	public void findByIdShouldReturnEmptyOptinalWhenIdDoesNotExist() {
		
		Optional<Product> result = repository.findById(notExintingId);
		Assertions.assertFalse(result.isPresent());
		
	}

}

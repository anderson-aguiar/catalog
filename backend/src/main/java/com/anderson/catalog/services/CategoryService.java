package com.anderson.catalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anderson.catalog.entities.Category;
import com.anderson.catalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	public List<Category> findAll(){
		return repository.findAll();
	}
}

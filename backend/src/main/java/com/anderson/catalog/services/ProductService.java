package com.anderson.catalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anderson.catalog.dto.ProductDTO;
import com.anderson.catalog.entities.Product;
import com.anderson.catalog.repositories.ProductRepository;
import com.anderson.catalog.services.exceptions.DatabaseException;
import com.anderson.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable){
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		//entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			//entity.setName(dto.getName());
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found - " + id);
		}

	}
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
			if(!repository.existsById(id)) {
				throw new ResourceNotFoundException("Recurso não encontrado");
			}
			try {
				repository.deleteById(id);
			}catch(DataIntegrityViolationException e) {
				throw new DatabaseException("Falha de integridade referencial");
			}
	}
}

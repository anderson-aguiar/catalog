package com.anderson.catalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.anderson.catalog.dto.ProductDTO;
import com.anderson.catalog.services.ProductService;
import com.anderson.catalog.services.exceptions.DatabaseException;
import com.anderson.catalog.services.exceptions.ResourceNotFoundException;
import com.anderson.catalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProductService service;
	
	private PageImpl<ProductDTO> page;
	private ProductDTO productDto;
	private Long existsId;
	private Long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	void setUp() throws Exception{
		existsId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		productDto = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDto));
		
		Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existsId)).thenReturn(productDto);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.eq(existsId), ArgumentMatchers.any())).thenReturn(productDto);
		Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		Mockito.doNothing().when(service).delete(existsId);
		Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDto);
	}
	
	@Test
	public void insertShouldReturnProductDTOCreated() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDto); // o metodo PUT tem que passar um body, esse objectMapper converte um Obj Java em String
		
		ResultActions result = 	mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());//acessa o objeto de resposta da requisição e verifica se o campo id existe
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() throws Exception {
		ResultActions result = 	mockMvc.perform(delete("/products/{id}", existsId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = 	mockMvc.perform(delete("/products/{id}", dependentId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDto); // o metodo PUT tem que passar um body, esse objectMapper converte um Obj Java em String
		
		ResultActions result = 	mockMvc.perform(put("/products/{id}", existsId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());//acessa o objeto de resposta da requisição e verifica se o campo id existe
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception  {
		String jsonBody = objectMapper.writeValueAsString(productDto); // o metodo PUT tem que passar um body, esse objectMapper converte um Obj Java em String
		
		ResultActions result = 	mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		//testar uma requisição no Controller em um teste unitário
		ResultActions result = 	mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
				
		result.andExpect(status().isOk());
	}
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		ResultActions result = 	mockMvc.perform(get("/products/{id}", existsId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());//acessa o objeto de resposta da requisição e verifica se o campo id existe
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = 	mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	
	
	
	
	
	
	
	
	
	
}

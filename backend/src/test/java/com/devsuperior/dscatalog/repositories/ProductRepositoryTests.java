package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository repository;
	
	private long existId;
	private long nonExistId;
	private long countTotalProducts;
	
	@BeforeEach
	void setup() throws Exception{
		existId = 1L;
		nonExistId = 2000L;
		countTotalProducts = 25L;
	}
	
	@Test 
	public void saveShouldPersistWhithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteWhenIdExists() {
		repository.deleteById(existId);
		
		Optional<Product> result = repository.findById(existId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		Optional<Product> result = repository.findById(existId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		Optional<Product> result = repository.findById(nonExistId);
		Assertions.assertTrue(result.isEmpty());
	}
}

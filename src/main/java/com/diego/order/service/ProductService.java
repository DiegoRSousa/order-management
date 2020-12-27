package com.diego.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diego.order.exception.DataIntegrityException;
import com.diego.order.exception.ObjectNotFoundException;
import com.diego.order.model.Product;
import com.diego.order.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(() 
				-> new ObjectNotFoundException(id, Product.class.getSimpleName()));
	}

	public List<Product> findByDescriptionLikeIgnoreCase(String description) {
		return productRepository.findByDescriptionLikeIgnoreCase(description);
	}
	
	public Product findByCode(String code) {
		return productRepository.findByCode(code);
	}

	public Product save(Product product) {
		if(productRepository.findByCode(product.getCode()) != null)
			throw new DataIntegrityException("Product with code: " + product.getCode() + "  already exists!");
		return productRepository.save(product);
	}
	
	public Product update(Product product) {
		var productValidation = productRepository.findByCode(product.getCode());
		if(productValidation != null && productValidation.getId() != product.getId())
			throw new DataIntegrityException("Product with code: " + product.getCode() + "  already exists!");
		return productRepository.save(product);
	}

	public void delete(Product product) {
		productRepository.delete(product);
	}
}

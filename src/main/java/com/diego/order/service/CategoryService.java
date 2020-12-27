package com.diego.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diego.order.exception.ConstraintException;
import com.diego.order.exception.ObjectNotFoundException;
import com.diego.order.model.Category;
import com.diego.order.repository.CategoryRepository;
import com.diego.order.repository.ProductRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	
	public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}
	
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
	
	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	
	public Category findById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() 
				-> new ObjectNotFoundException(id, Category.class.getSimpleName()));
	}

	public List<Category> findByDescriptionLike(String description) {
		return categoryRepository.findByDescriptionLike(description);
	}
	
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public Category update(Category category) {
		return categoryRepository.save(category);
	}
	
	public void delete(Category category) {
		var products = productRepository.findByCategory(category);
		if(!products.isEmpty())
			throw new ConstraintException("Category: " + category.getDescription() + " in use!"); 
						
		categoryRepository.delete(category);
	}
}
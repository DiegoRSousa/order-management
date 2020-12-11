package com.diego.order.service;

import org.springframework.stereotype.Service;

import com.diego.order.exception.ObjectNotFoundException;
import com.diego.order.model.Category;
import com.diego.order.repository.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public Category findById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, Category.class.getSimpleName()));
	}
}

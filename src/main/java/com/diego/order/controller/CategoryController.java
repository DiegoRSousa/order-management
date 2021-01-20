package com.diego.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.order.dto.CategoryRequest;
import com.diego.order.dto.CategoryResponse;
import com.diego.order.service.CategoryService;

@RestController
@RequestMapping("categories")
public class CategoryController {

	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping
	@Cacheable("findAllCategory")
	public ResponseEntity<List<CategoryResponse>> findAll() {
		var categories = categoryService.findAll().stream().map(CategoryResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/page")
	@Cacheable("findAllPageCategory")
	public ResponseEntity<Page<CategoryResponse>> findAll(
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		var categories = categoryService.findAll(pageable).map(CategoryResponse::new);
		return ResponseEntity.ok(categories);
	}	
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
		var category = categoryService.findById(id);
		return ResponseEntity.ok(new CategoryResponse(category));
	}
	
	@GetMapping("/description/{description}")
	public ResponseEntity<List<CategoryResponse>> findByDescription(@PathVariable String description) {
		var categories = categoryService.findByDescriptionLike(description)
							.stream().map(CategoryResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(categories);
	}
	
	@PostMapping
	@CacheEvict(value = {"findAllCategory", "findAllPageCategory"}, allEntries = true)
	public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryRequest categoryRequest) {
		var category = categoryService.save(categoryRequest.toModel());
		return new ResponseEntity<CategoryResponse>(new CategoryResponse(category), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@CacheEvict(value = {"findAllCategory", "findAllPageCategory"}, allEntries = true)
	public ResponseEntity<CategoryResponse> update(@PathVariable Long id, 
			@Valid @RequestBody CategoryRequest categoryRequest) {
		var category = categoryService.findById(id);
		category.update(categoryRequest.toModel());
		categoryService.update(category);
		return ResponseEntity.ok(new CategoryResponse(category));
	}
	
	@DeleteMapping("/{id}")
	@CacheEvict(value = {"findAllCategory", "findAllPageCategory"}, allEntries = true)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		var category = categoryService.findById(id);
		categoryService.delete(category);			
		return ResponseEntity.noContent().build();
	}
}
package com.diego.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
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

import com.diego.order.dto.ProductRequest;
import com.diego.order.dto.ProductResponse;
import com.diego.order.service.CategoryService;
import com.diego.order.service.ProductService;

@RestController
@RequestMapping("products")
public class ProductController {

	private final ProductService productService;
	private final CategoryService categoryService;
	
	public ProductController(ProductService productService, CategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAll() {
		var products = productService.findAll().stream().map(ProductResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<ProductResponse>> findAll(@ParameterObject
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		var products = productService.findAll(pageable).map(ProductResponse::new);
		return ResponseEntity.ok(products);
	}	
	
	@GetMapping("{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
		var product = productService.findById(id);
		return ResponseEntity.ok(new ProductResponse(product));
	}
	
	@GetMapping("/description/{description}")
	public ResponseEntity<List<ProductResponse>> findByDescriptionLikeIgnoreCase(@PathVariable String description) {
		var products = productService.findByDescriptionLikeIgnoreCase(description)
				.stream().map(ProductResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}
	
	@PostMapping
	public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest) {
		var category = categoryService.findById(productRequest.getCategoryId());
		var product = productService.save(productRequest.toModel(category));
		return new ResponseEntity<ProductResponse>(new ProductResponse(product), HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable Long id, 
			@Valid @RequestBody ProductRequest productRequest) {
		var product = productService.findById(id);
		var category = categoryService.findById(id);
		product.update(productRequest.toModel(category));
		productService.update(product);
		return ResponseEntity.ok(new ProductResponse(product));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		var product = productService.findById(id);
		productService.delete(product);
		return ResponseEntity.noContent().build();
	}
}

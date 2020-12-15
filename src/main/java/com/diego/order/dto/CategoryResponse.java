package com.diego.order.dto;

import java.time.LocalDateTime;

import com.diego.order.model.Category;

public class CategoryResponse {

	private Long id;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Deprecated
	public CategoryResponse() {}
	public CategoryResponse(Category category) {
		this.id = category.getId();
		this.description = category.getDescription();
		this.createdAt = category.getCreatedAt();
		this.updatedAt = category.getUpdatedAt();
	}
	
	public Long getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
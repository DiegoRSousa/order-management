package com.diego.order.dto;

import javax.validation.constraints.NotBlank;

import com.diego.order.model.Category;

public class CategoryRequest {

	@NotBlank(message = "field cannot blank")
	private String description;
	
	@Deprecated
	public CategoryRequest() {}

	public CategoryRequest(String description) {
		this.description = description;
	}

	public Category toModel() {
		return new Category(description);
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

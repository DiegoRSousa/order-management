package com.diego.order.dto;

import javax.validation.constraints.NotBlank;

import com.diego.order.model.Category;

public class CategoryRequest {

	@NotBlank(message = "field cannot blank")
	private String description;

	public Category toModel() {
		return new Category(description);
	}
	
	public String getDescription() {
		return description;
	}
}

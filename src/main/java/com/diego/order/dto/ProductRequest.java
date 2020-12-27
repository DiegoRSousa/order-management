package com.diego.order.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.diego.order.model.Category;
import com.diego.order.model.Product;

public class ProductRequest {
	
	@NotBlank(message = "field cannot blank")
	private String code;
	private String barcode;
	@NotBlank(message = "field cannot blank")
	private String description;
	@Positive
	private BigDecimal price;
	private BigDecimal quantity;
	private Long categoryId;
	
	@Deprecated
	public ProductRequest() {}

	public ProductRequest(@NotBlank(message = "field cannot blank") String code, String barcode,
			@NotBlank(message = "field cannot blank") String description, @Positive BigDecimal price,
			BigDecimal quantity, Long categoryId) {
		this.code = code;
		this.barcode = barcode;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.categoryId = categoryId;
	}
	
	public Product toModel(Category category) {
		return new Product(code, barcode, description, price, quantity, category);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	};
}

package com.diego.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.diego.order.model.Product;

public class ProductResponse {
	
	private Long id;
	private String code;
	private String barcode;
	private String description;
	private BigDecimal price;
	private BigDecimal quantity;
	private String category;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Deprecated
	public ProductResponse() {}
	public ProductResponse(Product product) {
		this.id = product.getId();
		this.code = product.getCode();
		this.barcode = product.getBarcode();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.quantity = product.getQuantity();
		this.category = product.getCategory().getDescription();
		this.createdAt = product.getCreatedAt();
		this.updatedAt = product.getUpdatedAt();
	}

	public Long getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getBarcode() {
		return barcode;
	}
	public String getDescription() {
		return description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public String getCategory() {
		return category;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
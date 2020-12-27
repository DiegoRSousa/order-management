package com.diego.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "field cannot blank")
	@Column(unique = true)
	private String code;
	private String barcode;
	@NotBlank(message = "field cannot blank")
	private String description;
	@Positive
	private BigDecimal price;
	private BigDecimal quantity;
	@ManyToOne
	private Category category;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime updatedAt;
	
	@Deprecated
	public Product() {}
	public Product(@NotBlank(message = "field cannot blank") String code, String barcode,
			@NotBlank(message = "field cannot blank") String description, @Positive BigDecimal price,
			BigDecimal quantity, Category category) {
		this.code = code;
		this.barcode = barcode;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}
	
	public void update(Product product) {
		this.code = product.code;
		this.barcode = product.barcode;
		this.description = product.description;
		this.price = product.price;
		this.quantity = product.quantity;
		this.category = product.category;
		this.updatedAt = LocalDateTime.now();
	}
	
	public Long getId() {
		return id;
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
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

package com.diego.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "field cannot blank")
	@Column(unique = true)
	private String cpf; 
	@NotBlank(message = "field cannot blank")
	private String name;
	@Embedded
	@NotNull(message = "field cannot null")
	private Address address;
	private boolean isBlocked;
	@Min(message = "The value must be greater than or equal to zero", value = 0)
	private BigDecimal creditLimit;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime updatedAt;

	@Deprecated
	public Customer() {}
	
	public Customer(@NotBlank(message = "field cannot blank") String cpf, @NotBlank(message = "field cannot blank") String name,
			@NotBlank(message = "field cannot blank") String cep, @NotBlank(message = "field cannot blank") String street,
			@NotBlank(message = "field cannot blank") String number, @NotBlank(message = "field cannot blank") String neighborhood,
			boolean isBlocked, @Min(message = "The value must be greater than or equal to zero", value = 0) BigDecimal creditLimit) {
		this.cpf = cpf;
		this.name = name;
		this.address = new Address(cep, street, number, neighborhood);
		this.isBlocked = isBlocked;
		this.creditLimit = creditLimit;
		
	}
	
	public void update(Customer customer) {
		this.cpf = customer.getCpf();
		this.name = customer.getName();
		this.address = customer.getAddress();
		this.isBlocked = customer.isBlocked();
		this.creditLimit = customer.getCreditLimit();
		this.updatedAt = LocalDateTime.now();
	}
	
	public String getCpf() {
		return this.cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress
	() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Long getId() {
		return id;
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
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

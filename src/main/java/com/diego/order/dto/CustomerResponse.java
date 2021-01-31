package com.diego.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.diego.order.model.Customer;

public class CustomerResponse {
	
	private Long id;
	private String cpf; 
	private String name;
	private String cep;
	private String street;
	private String number;
	private String neighborhood;
	private boolean isBlocked;
	private BigDecimal creditLimit;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Deprecated
	public CustomerResponse() {}

	public CustomerResponse(Customer customer) {
		this.id = customer.getId();
		this.cpf = customer.getCpf();
		this.name = customer.getName();
		this.cep = customer.getAddress().getCep();
		this.street = customer.getAddress().getStreet();
		this.number = customer.getAddress().getNumber();
		this.neighborhood = customer.getAddress().getNeighborhood();
		this.isBlocked = customer.isBlocked();
		this.creditLimit = customer.getCreditLimit();
		this.createdAt = customer.getCreatedAt();
		this.updatedAt = customer.getUpdatedAt();
	}

	public Long getId() {
		return id;
	}

	public String getCpf() {
		return cpf;
	}

	public String getName() {
		return name;
	}

	public String getCep() {
		return cep;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
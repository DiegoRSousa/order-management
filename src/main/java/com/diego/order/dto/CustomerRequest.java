package com.diego.order.dto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.diego.order.model.Customer;


public class CustomerRequest {

	@NotBlank(message = "field cannot blank")
	@Column(unique = true)
	private String cpf; 
	@NotBlank(message = "field cannot blank")
	private String name;
	@NotNull(message = "field cannot null")
	@NotBlank(message = "field cannot blank")
	private String cep;
	@NotBlank(message = "field cannot blank")
	private String street;
	@NotBlank(message = "field cannot blank")
	private String number;
	@NotBlank(message = "field cannot blank")
	private String neighborhood;
	private boolean isBlocked;
	@Positive(message = "The value must be greater than zero")
	private BigDecimal creditLimit;
	
	public Customer toModel() {
		return new Customer(cpf, name, cep, street, number, neighborhood, isBlocked, creditLimit);
	}
	public String getCpf() {
		return cpf;
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
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
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
}

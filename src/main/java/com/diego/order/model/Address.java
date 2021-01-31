package com.diego.order.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Address {
	@NotBlank(message = "field cannot blank")
	private String cep;
	@NotBlank(message = "field cannot blank")
	private String street;
	@NotBlank(message = "field cannot blank")
	private String number;
	@NotBlank(message = "field cannot blank")
	private String neighborhood;
	
	@Deprecated
	public Address() {}
	public Address(@NotBlank(message = "field cannot blank") String cep, @NotBlank(message = "field cannot blank") String street,
	@NotBlank(message = "field cannot blank") String number, @NotBlank(message = "field cannot blank") String neighborhood) {
		this.cep = cep;
		this.street = street;
		this.number = number;
		this.neighborhood = neighborhood;
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
}

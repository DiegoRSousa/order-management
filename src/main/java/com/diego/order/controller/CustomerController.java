package com.diego.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.order.dto.CustomerRequest;
import com.diego.order.dto.CustomerResponse;
import com.diego.order.exception.DataIntegrityException;
import com.diego.order.exception.ObjectNotFoundException;
import com.diego.order.model.Customer;
import com.diego.order.repository.CustomerRepository;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerRepository customerRepository;
	
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@GetMapping
	public ResponseEntity<List<CustomerResponse>> findAll() {
		var customers = customerRepository.findAll().stream().map(CustomerResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(customers);
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<CustomerResponse>> findAll(@ParameterObject
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		var customers = customerRepository.findAll(pageable).map(CustomerResponse::new);
		return ResponseEntity.ok(customers);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
		var customer = customerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, Customer.class.getSimpleName()));
		return ResponseEntity.ok(new CustomerResponse(customer));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<CustomerResponse>> findByName(@PathVariable String name) {
		var customers = customerRepository.findByNameLikeIgnoreCase(name).stream().map(CustomerResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(customers);
	}
	
	@PostMapping
	public ResponseEntity<CustomerResponse> save(@Valid @RequestBody CustomerRequest customerRequest) {
		var customerValidation = customerRepository.findByCpf(customerRequest.getCpf());
		if(customerValidation != null)
			throw new DataIntegrityException("customer with code: " + customerRequest.getCpf() + "  already exists!");
		var customer = customerRepository.save(customerRequest.toModel());
		return new ResponseEntity<CustomerResponse>(new CustomerResponse(customer), HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<CustomerResponse> update(@Valid @PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
		var customer = customerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, Customer.class.getSimpleName()));
		var customerValidation = customerRepository.findByCpf(customerRequest.getCpf());
		if(customerValidation != null && customerValidation.getId() != customer.getId())
			throw new DataIntegrityException("customer with code: " + customerRequest.getCpf() + "  already exists!");
		customer.update(customerRequest.toModel());
		customerRepository.save(customer);
		return ResponseEntity.ok(new CustomerResponse(customer));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		var customer = customerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, Customer.class.getSimpleName()));
		customerRepository.delete(customer);
		return ResponseEntity.noContent().build();
	}
}

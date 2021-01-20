package com.diego.order.integration;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.diego.order.controller.exception.StandardError;
import com.diego.order.controller.exception.ValidationError;
import com.diego.order.dto.ProductRequest;
import com.diego.order.dto.ProductResponse;
import com.diego.order.model.Category;
import com.diego.order.model.Product;
import com.diego.order.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductControllerIT {

	@Autowired
	private TestRestTemplate testRestTemplate;
	Product product;
	ProductRequest productRequest;
	
	@BeforeEach
	void setup() {
		var category = new Category("Bebidas");
		product = new Product("0001", null, "Regrigerante Lata", new BigDecimal(4.00), 
						new BigDecimal(4.00), category);
		productRequest = new ProductRequest("0100", "1233", "Regrigerante Lata", new BigDecimal(4.00), 
						new BigDecimal(4.00), 4L);
	}
	@Test
	void findAll_ReturnsListOfProductInsidePageObject_WhenSuccessful() {
		var response = testRestTemplate.exchange("/products/page", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Product>>() {});
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().toList().get(0)
				.getDescription()).isEqualTo(product.getDescription());
	}
	
	@Test
	void findAll_ReturnsListOfProduct_WhenSuccessful() {
		var response = testRestTemplate.exchange("/products", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Product>>() {});
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().get(0)
				.getDescription()).isEqualTo(product.getDescription());
	}
	
	@Test
	void findById_ReturnsProduct_WhenSuccessful() {
		var response = testRestTemplate.getForEntity("/products/1", Product.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getDescription()).isEqualTo(product.getDescription());
		Assertions.assertThat(response.getBody().getId()).isEqualTo(1);
	}
	
	@Test
	void findById_ThrowObjectNotFoundException_WhenProductNotFound() {
		var response = testRestTemplate.getForEntity("/products/99", StandardError.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getError())
			.isNotNull()
			.isEqualTo("Not found");
	}
	
	@Test
	void findByDescription_ReturnsListOfProduct_WhenSuccessful() {
		var response = testRestTemplate.exchange("/products/description/Piz%", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Product>>() {});

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull().isNotEmpty().hasSize(2);
		Assertions.assertThat(response.getBody().get(0).getDescription()).isEqualTo("Pizza Pequena");
	}
	
	@Test
	void findByDescription_ReturnsEmpty_WhenProductNotFound() {
		var response = testRestTemplate.exchange("/products/description/Tes%", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Product>>() {});

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void save_ReturnsProduct_WhenSuccessful() {
		var expectedDescription = productRequest.getDescription();
		var response = testRestTemplate.postForEntity("/products", productRequest, ProductResponse.class);
				
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(response.getBody().getCreatedAt()).isNotNull();
		Assertions.assertThat(response.getBody().getUpdatedAt()).isNull();
	}
	
	@Test
	void save_ReturnsDataIntegrityException_WhenUsuccessful() {
		productRequest.setCode("0001");
		var response = testRestTemplate.postForEntity("/products", productRequest, ValidationError.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getError()).isNotNull().isEqualTo("Data Integrity");
	}
	
	@Test
	void save_ReturnsBadRequest_WhenUnsuccessful() {
		productRequest.setDescription("");
		var response = testRestTemplate.postForEntity("/products", productRequest, ValidationError.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getError()).isNotNull().isEqualTo("Validation Error");
		Assertions.assertThat(response.getBody().getErrors().get(0).getFieldName()).isEqualTo("description");
		Assertions.assertThat(response.getBody().getErrors().get(0).getMessage()).isEqualTo("field cannot blank");
	}
	
	@Test
	void update_ReturnsProduct_WhenSuccessful() {
		var expectedDescription = productRequest.getDescription();
		var response = testRestTemplate.exchange("/products/1", HttpMethod.PUT, 
				new HttpEntity<>(productRequest), ProductResponse.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(response.getBody().getCreatedAt()).isNotNull();
		Assertions.assertThat(response.getBody().getUpdatedAt()).isNotNull();
	}
	
	@Test
	void update_ReturnsBadRequest_WhenSuccessful() {
		productRequest.setCode("0002");;
		var response = testRestTemplate.exchange("/products/1", HttpMethod.PUT, 
				new HttpEntity<>(productRequest), ValidationError.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(response.getBody().getError()).isEqualTo("Data Integrity");
		Assertions.assertThat(response.getBody().getMessage()).isEqualTo("Product with code: 0002  already exists!");
	}
	
	@Test
	void delete_ReturnsNoContent_WhenSuccessful() {
		var response = testRestTemplate.exchange("/products/6", HttpMethod.DELETE, 
				null, Void.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThat(response.getBody()).isNull();
	}
}
package com.diego.order.integration;

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
import com.diego.order.dto.CategoryRequest;
import com.diego.order.dto.CategoryResponse;
import com.diego.order.model.Category;
import com.diego.order.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryControllerIT {

	@Autowired
	private TestRestTemplate testRestTemplate;
	private Category category;
	private CategoryRequest categoryRequest;

	@BeforeEach
	void setup() {
		category = new Category("Pizzas");
		categoryRequest = new CategoryRequest("Sucos");
	}

	@Test
	void findAll_ReturnsListOfCategoryInsidePageObject_WhenSuccessful() {
		var expectedDescription = category.getDescription();

		var response = testRestTemplate.exchange("/categories/page", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Category>>() {
				});

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().toList().get(0).getDescription()).isEqualTo(expectedDescription);
	}

	@Test
	void findAll_ReturnsListOfCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();

		var response = testRestTemplate.exchange("/categories", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Category>>() {
				});

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody()).isNotEmpty().hasSize(6);
		Assertions.assertThat(response.getBody().get(0).getDescription()).isEqualTo(expectedDescription);
	}

	@Test
	void findById_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();

		var response = testRestTemplate.getForEntity("/categories/1", Category.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getDescription()).isEqualTo(expectedDescription);
		Assertions.assertThat(response.getBody().getId()).isEqualTo(1);
	}

	@Test
	void findById_ThrowObjectNotFoundException_WhenCategoryNotFound() {
		var response = testRestTemplate.getForEntity("/categories/11", StandardError.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getError())
			.isNotNull()
			.isEqualTo("Not found");
	}

	@Test
	void findByDescription_ReturnsListOfCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();

		var response = testRestTemplate.exchange("/categories/description/Piz%", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Category>>() {});

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull().isNotEmpty().hasSize(1);
		Assertions.assertThat(response.getBody().get(0).getDescription()).isEqualTo(expectedDescription);
	}

	@Test
	void findByDescription_ReturnsEmpty_WhenCategoryNotFound() {
		var response = testRestTemplate.exchange("/categories/description/Tes%", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Category>>() {});

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void save_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = categoryRequest.getDescription();
		var response = testRestTemplate.postForEntity("/categories", categoryRequest, CategoryResponse.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(response.getBody().getCreatedAt()).isNotNull();
		Assertions.assertThat(response.getBody().getUpdatedAt()).isNull();
	}
	
	@Test
	void save_ReturnsObjectNotFoundException_WhenNotFound() {
		categoryRequest.setDescription("");
		var response = testRestTemplate.postForEntity("/categories", categoryRequest, ValidationError.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getError()).isNotNull().isEqualTo("Validation Error");
		Assertions.assertThat(response.getBody().getErrors().get(0).getFieldName()).isEqualTo("description");
		Assertions.assertThat(response.getBody().getErrors().get(0).getMessage()).isEqualTo("field cannot blank");
	}
	
	@Test
	void update_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = categoryRequest.getDescription();
		var response = testRestTemplate.exchange("/categories/1", HttpMethod.PUT, 
				new HttpEntity<>(categoryRequest), CategoryResponse.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(response.getBody().getCreatedAt()).isNotNull();
		Assertions.assertThat(response.getBody().getUpdatedAt()).isNotNull();
	}
	
	@Test
	void delete_ReturnsNoContent_WhenSuccessful() {
		var response = testRestTemplate.exchange("/categories/6", HttpMethod.DELETE, 
				null, Void.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThat(response.getBody()).isNull();
	}
	
	@Test
	void delete_ReturnsBadRequest_WhenUnsuccessful() {
		var response = testRestTemplate.exchange("/categories/4", HttpMethod.DELETE, 
				null, Void.class);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(response.getBody()).isNull();
	}
}

package com.diego.order.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.diego.order.dto.CategoryRequest;
import com.diego.order.exception.ObjectNotFoundException;
import com.diego.order.model.Category;
import com.diego.order.service.CategoryService;

@ExtendWith(SpringExtension.class)
class CategoryControllerTest {

	@InjectMocks
	private CategoryController categoryController;
	@Mock
	private CategoryService categoryService;
	private Category category;
	private CategoryRequest categoryRequest;
	
	@BeforeEach
	void setup() {
		 category = new Category("Pizza");
		 categoryRequest = new CategoryRequest("Lanche");
		 
		 BDDMockito.when(categoryService.findAll(ArgumentMatchers.any()))
		 	.thenReturn(new PageImpl<Category>(List.of(category)));
		 BDDMockito.when(categoryService.findAll())
		 	.thenReturn(List.of(category));
		 BDDMockito.when(categoryService.findById(ArgumentMatchers.anyLong()))
		 	.thenReturn(category);
		 BDDMockito.when(categoryService.findByDescriptionLike(ArgumentMatchers.any()))
		 	.thenReturn(List.of(category));
		 BDDMockito.when(categoryService.save(ArgumentMatchers.any(Category.class)))
		 	.thenReturn(category);
		 BDDMockito.when(categoryService.update(ArgumentMatchers.any(Category.class)))
		 	.thenReturn(category);
		 BDDMockito.doNothing().when(categoryService).delete(ArgumentMatchers.any());
	}
	
	@Test
	void findAll_ReturnsListOfCategoryInsidePageObject_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var request = categoryController.findAll(null);
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(request.getBody()).isNotNull();
		Assertions.assertThat(request.getBody().toList())
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(request.getBody().toList().get(0).getDescription()).isEqualTo(expectedDescription);
	}
	
	@Test
	void findAll_ReturnsListOfCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var request = categoryController.findAll();
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(request.getBody())
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(request.getBody().get(0).getDescription()).isEqualTo(expectedDescription);
	}
	
	@Test
	void findById_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var expectedId = category.getId();
		var request = categoryController.findById(1l);
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(request.getBody()).isNotNull();
		Assertions.assertThat(request.getBody().getDescription()).isEqualTo(expectedDescription);
		Assertions.assertThat(request.getBody().getId()).isEqualTo(expectedId);
	}
	
	@Test
	void findById_ThrowObjectNotFoundException_WhenCategoryNotFound() {
		BDDMockito.doThrow(ObjectNotFoundException.class)
			.when(categoryService).findById(ArgumentMatchers.anyLong());
		
		Assertions.assertThatExceptionOfType(ObjectNotFoundException.class)
			.isThrownBy(() -> categoryController.findById(1L));
	}
	
	@Test
	void findByDescription_ReturnsListOfCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var request = categoryController.findAll();
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(request.getBody())
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(request.getBody().get(0).getDescription()).isEqualTo(expectedDescription);
	}
	
	@Test
	void findByDescription_ReturnsEmpty_WhenCategoryNotFound() {
		BDDMockito.when(categoryService.findByDescriptionLike(ArgumentMatchers.any()))
	 	.thenReturn(Collections.emptyList());
		
		var request = categoryController.findByDescription(null);
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(request.getBody())
			.isNotNull()
			.isEmpty();
	}
	
	@Test
	void save_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var request = categoryController.save(categoryRequest);
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(request.getBody()).isNotNull();
		Assertions.assertThat(request.getBody().getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(request.getBody().getCreatedAt()).isNotNull();
		Assertions.assertThat(request.getBody().getUpdatedAt()).isNull();
	}
	
	@Test
	void save_ReturnsObjectNotFoundException_WhenNotFound() {
		BDDMockito.when(categoryService.save(ArgumentMatchers.any(Category.class)))
			.thenThrow(ConstraintViolationException.class);
	
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> categoryController.save(categoryRequest));
	}
	
	@Test
	void update_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = categoryRequest.getDescription();
		var request = categoryController.update(1L, categoryRequest);
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(request.getBody()).isNotNull();
		Assertions.assertThat(request.getBody().getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(request.getBody().getCreatedAt()).isNotNull();
	}
	
	@Test
	void delete_ReturnsNoContent_WhenSuccessful() {
		var request = categoryController.delete(1L);
		
		Assertions.assertThat(request.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThat(request.getBody()).isNull();
		
	}
}
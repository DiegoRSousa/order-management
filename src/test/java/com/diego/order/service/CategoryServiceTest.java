package com.diego.order.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.diego.order.exception.ObjectNotFoundException;
import com.diego.order.model.Category;
import com.diego.order.repository.CategoryRepository;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	@InjectMocks
	private CategoryService categoryService;
	@Mock
	private CategoryRepository categoryRepository;
	private Category category;
	
	
	@BeforeEach
	void setup() {
		 category = new Category("Pizza");
		 		 
		 BDDMockito.when(categoryRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
		 	.thenReturn(new PageImpl<Category>(List.of(category)));
		 BDDMockito.when(categoryRepository.findAll()).thenReturn(List.of(category));
		 BDDMockito.when(categoryRepository.findById(ArgumentMatchers.anyLong()))
		 	.thenReturn(Optional.of(category));
		 BDDMockito.when(categoryRepository.findByDescriptionLike(ArgumentMatchers.any()))
		 	.thenReturn(List.of(category));
		 BDDMockito.when(categoryRepository.save(ArgumentMatchers.any(Category.class)))
		 	.thenReturn(category);
		 BDDMockito.doNothing().when(categoryRepository).delete(ArgumentMatchers.any());
	}
	
	@Test
	void findAll_ReturnsListOfCategoryInsidePageObject_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var categories = categoryService.findAll(PageRequest.of(1, 1));
		
		Assertions.assertThat(categories)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(categories.toList().get(0).getDescription()).isEqualTo(expectedDescription);
	}
	
	@Test
	void findAll_ReturnsListOfCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var categories = categoryService.findAll();
		
		Assertions.assertThat(categories)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(categories.get(0).getDescription()).isEqualTo(expectedDescription);
	}
	
	@Test
	void findById_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var expectedId = category.getId();
		var categories = categoryService.findById(1l);
		
		Assertions.assertThat(categories).isNotNull();
		Assertions.assertThat(categories.getDescription()).isEqualTo(expectedDescription);
		Assertions.assertThat(categories.getId()).isEqualTo(expectedId);
	}
	
	@Test
	void findById_ThrowsObjectNotFoundException_WhenNotFound() {
		BDDMockito.when(categoryRepository.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.empty());
		
		Assertions.assertThatExceptionOfType(ObjectNotFoundException.class)
			.isThrownBy(() -> categoryService.findById(1L));
	}
	
	@Test
	void findByDescription_ReturnsListOfCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var categories = categoryService.findAll();
		
		Assertions.assertThat(categories)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(categories.get(0).getDescription()).isEqualTo(expectedDescription);
	}
	
	@Test
	void findByDescription_ReturnsEmpty_WhenCategoryNotFound() {
		BDDMockito.when(categoryService.findByDescriptionLike(ArgumentMatchers.any()))
	 	.thenReturn(Collections.emptyList());
		
		var categories = categoryService.findByDescriptionLike(null);
		
		Assertions.assertThat(categories).isNotNull().isEmpty();
	}
	
	@Test
	void save_ReturnsCategory_WhenSuccessful() {
		var expectedDescription = category.getDescription();
		var savedCategory = categoryService.save(category);
	
		Assertions.assertThat(savedCategory).isNotNull();
		Assertions.assertThat(savedCategory.getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(savedCategory.getCreatedAt()).isNotNull();
		Assertions.assertThat(savedCategory.getUpdatedAt()).isNull();
	}
	
	@Test
	void save_ThrowsConstraintViolationException_WhenDescriptionIsEmpty() {
		BDDMockito.when(categoryRepository.save(ArgumentMatchers.any(Category.class)))
			.thenThrow(ConstraintViolationException.class);
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.categoryService.save(category));
	}
	
	@Test
	void update_ReturnsCategory_WhenSuccessful() {
		category.setDescription("Lanche");
		var expectedDescription = category.getDescription();
		var updatedCategory = categoryService.update(category);
		
		Assertions.assertThat(updatedCategory).isNotNull();
		Assertions.assertThat(updatedCategory.getDescription())
			.isNotNull()
			.isEqualTo(expectedDescription);
		Assertions.assertThat(updatedCategory.getCreatedAt()).isNotNull();
	}
	
	@Test
	void delete_ReturnsNoContent_WhenSuccessful() {
		Assertions.assertThatCode(() -> categoryService.delete(category))
		.doesNotThrowAnyException();
	}
}
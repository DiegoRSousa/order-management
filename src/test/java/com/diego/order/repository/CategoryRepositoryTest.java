package com.diego.order.repository;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.diego.order.model.Category;

@DataJpaTest
@DisplayName("Tests for Category Repository")
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;
	private Category category;
	
	@BeforeEach
	void setup() {
		category = categoryRepository.save(new Category("Pizza"));
	}
	
	@Test
	void save_PersistCategory_WhenSuccessful() {
		Assertions.assertThat(category).isNotNull();
		Assertions.assertThat(category.getId()).isNotNull();
		Assertions.assertThat(category.getDescription()).isNotNull();
		Assertions.assertThat(category.getCreatedAt()).isNotNull();
		Assertions.assertThat(category.getUpdatedAt()).isNull();
	}
	
	@Test
	void save_UpdateCategory_WhenSuccessful() {
		category.setDescription("Suco");
		category.update(category);
		
		Assertions.assertThat(category).isNotNull();
		Assertions.assertThat(category.getId()).isNotNull();
		Assertions.assertThat(category.getDescription()).isEqualTo(category.getDescription());
		Assertions.assertThat(category.getCreatedAt()).isNotNull();
		Assertions.assertThat(category.getUpdatedAt()).isNotNull();
	}
	
	@Test
	void delete_RemoveCategory_WhenSuccessful() {
		categoryRepository.delete(category);
		var categoryOptional = categoryRepository.findById(category.getId());
		
		Assertions.assertThat(categoryOptional).isEmpty();
	}
	
	@Test
	void findByName_FoundCategory_WhenSuccessful() {
		var description = category.getDescription();
		var categories = categoryRepository.findByDescriptionLike(description);
		
		Assertions.assertThat(categories).isNotEmpty().contains(category);
	}
	
	@Test
	void findByName_NotFoundCategory_WhenSuccessful() {
		var categories = categoryRepository.findByDescriptionLike("Lanche");
		
		Assertions.assertThat(categories).isEmpty();
		Assertions.assertThat(categories).doesNotContain(category);
	}
	
	@Test
	void save_ThrowsConstraintViolationException_WhenDescriptionIsEmpty() {
		var categoryValidation = new Category("");
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.categoryRepository.save(categoryValidation))
			.withMessageContaining("field cannot blank");
	}
}
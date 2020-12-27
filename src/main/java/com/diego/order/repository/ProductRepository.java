package com.diego.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diego.order.model.Category;
import com.diego.order.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByDescriptionLikeIgnoreCase(String description);
	Product findByCode(String code);
	List<Product> findByCategory(Category category);
}

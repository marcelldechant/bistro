package com.github.marcelldechant.bistro.product.repository;

import com.github.marcelldechant.bistro.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Product entities.
 * This interface extends JpaRepository to provide CRUD operations for Product entities.
 * It is annotated with @Repository to indicate that it is a Spring Data repository.
 *
 * @author Marcell Dechant
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

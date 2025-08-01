package com.github.marcelldechant.bistro.product.repository;

import com.github.marcelldechant.bistro.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

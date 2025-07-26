package com.github.marcelldechant.bistro.order.repository;

import com.github.marcelldechant.bistro.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Order entities.
 * This interface extends JpaRepository to provide CRUD operations for Order entities.
 * It is annotated with @Repository to indicate that it is a Spring Data repository.
 *
 * @author Marcell Dechant
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

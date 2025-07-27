package com.github.marcelldechant.bistro.order.repository;

import com.github.marcelldechant.bistro.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

package com.github.marcelldechant.bistro.order.entity;

import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents an order in the Bistro application.
 * This class contains details about the order such as table number, items, subtotal,
 * discount, total amount, and whether it's a happy hour order.
 *
 * @author Marcell Dechant
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal total;

    private boolean isHappyHour;

}

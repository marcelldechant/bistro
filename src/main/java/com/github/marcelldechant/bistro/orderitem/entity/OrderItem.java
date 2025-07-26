package com.github.marcelldechant.bistro.orderitem.entity;

import com.github.marcelldechant.bistro.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents an item in an order in the Bistro application.
 * Each OrderItem is associated with a Product and contains information about the quantity,
 * price per unit, and total price.
 *
 * @author Marcell Dechant
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @Min(value = 1, message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal totalPrice;

}

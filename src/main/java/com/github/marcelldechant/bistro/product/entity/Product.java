package com.github.marcelldechant.bistro.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a product in the Bistro application.
 *
 * @author Marcell Dechant
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2-100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be positive")
    @Digits(integer = 5, fraction = 2, message = "Price must have max 5 integer and 2 fraction digits")
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

}

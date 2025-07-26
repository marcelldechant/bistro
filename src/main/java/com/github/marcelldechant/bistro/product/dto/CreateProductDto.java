package com.github.marcelldechant.bistro.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new product.
 * It contains the necessary fields to create a product in the Bistro application.
 *
 * @param name  the name of the product
 * @param price the price of the product
 * @author Marcell Dechant
 */
public record CreateProductDto(
        @NotBlank(message = "Product name is mandatory")
        @Size(min = 2, max = 100, message = "Name must be between 2-100 characters")
        String name,

        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price must be positive")
        @Digits(integer = 5, fraction = 2, message = "Price must have max 5 integer and 2 fraction digits")
        BigDecimal price
) {
}

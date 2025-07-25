package de.deichmann.bistro.product.dto;

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
        String name,
        BigDecimal price
) {
}

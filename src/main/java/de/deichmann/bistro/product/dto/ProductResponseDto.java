package de.deichmann.bistro.product.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for product responses.
 * This class is used to transfer product data in a simplified format.
 * It contains the product's ID, name, and price.
 *
 * @param id    the unique identifier of the product
 * @param name  the name of the product
 * @param price the price of the product
 * @author Marcell Dechant
 */
public record ProductResponseDto(
        Long id,
        String name,
        BigDecimal price
) {
}

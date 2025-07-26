package com.github.marcelldechant.bistro.orderitem.dto;

import com.github.marcelldechant.bistro.product.entity.Product;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for order item responses.
 * This class is used to transfer order item data in a simplified format.
 * It contains the order item's ID, associated product, quantity, price per unit, and total price.
 *
 * @param id           the unique identifier of the order item
 * @param product      the product associated with the order item
 * @param quantity     the quantity of the product in the order item
 * @param pricePerUnit the price per unit of the product
 * @param totalPrice   the total price for the quantity of the product
 * @author Marcell Dechant
 */
public record OrderItemResponseDto(
        Long id,
        Product product,
        int quantity,
        BigDecimal pricePerUnit,
        BigDecimal totalPrice
) {
}

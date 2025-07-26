package com.github.marcelldechant.bistro.orderitem.dto;

/**
 * Data Transfer Object for creating an order item.
 * This class is used to encapsulate the necessary information for creating an order item,
 * including the product ID and the quantity of the product.
 *
 * @param productId the ID of the product being ordered
 * @param quantity  the quantity of the product being ordered
 * @author Marcell Dechant
 */
public record CreateOrderItemDto(
        Long productId,
        int quantity
) {
}

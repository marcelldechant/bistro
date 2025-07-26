package com.github.marcelldechant.bistro.orderitem.mapper;

import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.product.entity.Product;

import java.math.BigDecimal;

/**
 * Mapper class for converting between OrderItem entities and DTOs.
 * This class provides static methods to convert CreateOrderItemDto to OrderItem entity
 * and OrderItem entity to OrderItemResponseDto.
 * It also includes a method to calculate the total price based on quantity and product price.
 */
public class OrderItemMapper {

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used statically, so no instances should be created.
     */
    private OrderItemMapper() {
    }

    /**
     * Converts a CreateOrderItemDto to an OrderItem entity.
     *
     * @param dto     the CreateOrderItemDto to convert
     * @param product the Product associated with the order item
     * @return an OrderItem entity with the product, quantity, price per unit, and total price
     */
    public static OrderItem toEntity(CreateOrderItemDto dto, Product product) {
        return OrderItem.builder()
                .product(product)
                .quantity(dto.quantity())
                .pricePerUnit(product.getPrice())
                .totalPrice(calculateTotalPrice(dto.quantity(), product))
                .build();
    }

    /**
     * Converts an OrderItem entity to an OrderItemResponseDto.
     *
     * @param entity the OrderItem entity to convert
     * @return an OrderItemResponseDto containing the order item details
     */
    public static OrderItemResponseDto fromEntity(OrderItem entity) {
        return new OrderItemResponseDto(
                entity.getId(),
                entity.getProduct(),
                entity.getQuantity(),
                entity.getPricePerUnit(),
                entity.getTotalPrice()
        );
    }

    /**
     * Calculates the total price for an order item based on quantity and product price.
     *
     * @param quantity the quantity of the product
     * @param product  the Product entity containing the price
     * @return the total price as a BigDecimal
     */
    private static BigDecimal calculateTotalPrice(int quantity, Product product) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

}

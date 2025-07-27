package com.github.marcelldechant.bistro.orderitem.mapper;

import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.product.entity.Product;

import java.math.BigDecimal;

public class OrderItemMapper {
    private OrderItemMapper() {
    }

    public static OrderItem toEntity(CreateOrderItemDto dto, Product product) {
        return OrderItem.builder()
                .product(product)
                .quantity(dto.quantity())
                .pricePerUnit(product.getPrice())
                .totalPrice(calculateTotalPrice(dto.quantity(), product))
                .build();
    }

    public static OrderItemResponseDto fromEntity(OrderItem entity) {
        return new OrderItemResponseDto(
                entity.getId(),
                entity.getProduct(),
                entity.getQuantity(),
                entity.getPricePerUnit(),
                entity.getTotalPrice()
        );
    }

    private static BigDecimal calculateTotalPrice(int quantity, Product product) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}

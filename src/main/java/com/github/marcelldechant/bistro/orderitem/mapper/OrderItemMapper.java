package com.github.marcelldechant.bistro.orderitem.mapper;

import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;

import java.math.BigDecimal;

public class OrderItemMapper {

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used statically, so no instances should be created.
     */
    private OrderItemMapper() {
    }

    public static OrderItem toEntity(CreateOrderItemDto dto) {
        return OrderItem.builder()
                .product(dto.product())
                .quantity(dto.quantity())
                .pricePerUnit(dto.product().getPrice())
                .totalPrice(calculateTotalPrice(dto))
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

    private static BigDecimal calculateTotalPrice(CreateOrderItemDto dto) {
        return dto.product().getPrice().multiply(BigDecimal.valueOf(dto.quantity()));
    }

}

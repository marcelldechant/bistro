package com.github.marcelldechant.bistro.orderitem.dto;

public record CreateOrderItemDto(
        Long productId,
        int quantity
) {
}

package com.github.marcelldechant.bistro.orderitem.dto;

import com.github.marcelldechant.bistro.product.entity.Product;

public record CreateOrderItemDto(
        Product product,
        int quantity
) {
}

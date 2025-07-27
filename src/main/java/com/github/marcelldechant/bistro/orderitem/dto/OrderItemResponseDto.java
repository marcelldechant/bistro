package com.github.marcelldechant.bistro.orderitem.dto;

import com.github.marcelldechant.bistro.product.entity.Product;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long id,
        Product product,
        int quantity,
        BigDecimal pricePerUnit,
        BigDecimal totalPrice
) {
}

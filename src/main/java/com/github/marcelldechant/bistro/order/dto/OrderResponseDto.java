package com.github.marcelldechant.bistro.order.dto;

import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDto(
        Long id,
        int tableNumber,
        List<OrderItemResponseDto> items,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal total,
        boolean isHappyHour
) {
}

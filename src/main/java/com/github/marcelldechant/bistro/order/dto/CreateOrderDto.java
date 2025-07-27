package com.github.marcelldechant.bistro.order.dto;

import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;

import java.util.List;

public record CreateOrderDto(
        int tableNumber,
        List<CreateOrderItemDto> items
) {
}


package com.github.marcelldechant.bistro.order.mapper;

import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.orderitem.mapper.OrderItemMapper;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {
    private OrderMapper() {
    }

    public static Order toEntity(int tableNumber,
                                 List<OrderItem> items,
                                 BigDecimal subtotal,
                                 BigDecimal discount,
                                 BigDecimal total,
                                 boolean isHappyHour) {

        return Order.builder()
                .tableNumber(tableNumber)
                .items(items)
                .subtotal(subtotal)
                .discount(discount)
                .total(total)
                .isHappyHour(isHappyHour)
                .build();
    }

    public static OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getTableNumber(),
                order.getItems().stream().map(OrderItemMapper::fromEntity).toList(),
                order.getSubtotal(),
                order.getDiscount(),
                order.getTotal(),
                order.isHappyHour()
        );
    }
}

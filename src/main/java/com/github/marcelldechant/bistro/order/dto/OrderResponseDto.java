package com.github.marcelldechant.bistro.order.dto;

import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object (DTO) for order responses.
 * This class is used to transfer order data in a simplified format.
 * It contains the order's ID, table number, list of order items, subtotal, discount, total amount, and whether it's happy hour.
 *
 * @param id          the unique identifier of the order
 * @param tableNumber the table number associated with the order
 * @param orderItems  the list of items in the order
 * @param subtotal    the subtotal amount of the order
 * @param discount    any discount applied to the order
 * @param total       the total amount of the order after applying discounts
 * @param isHappyHour indicates if the order was placed during happy hour
 * @author Marcell Dechant
 */
public record OrderResponseDto(
        Long id,
        int tableNumber,
        List<OrderItemResponseDto> orderItems,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal total,
        boolean isHappyHour
) {
}

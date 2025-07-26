package com.github.marcelldechant.bistro.order.dto;

import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;

import java.util.List;

/**
 * Data Transfer Object for creating an order.
 * This class is used to encapsulate the details required to create a new order,
 * including the table number and a list of items in the order.
 *
 * @param tableNumber the number of the table for which the order is being created
 * @param items       a list of items included in the order
 * @author Marcell Dechant
 */
public record CreateOrderDto(
        int tableNumber,
        List<CreateOrderItemDto> items
) {
}


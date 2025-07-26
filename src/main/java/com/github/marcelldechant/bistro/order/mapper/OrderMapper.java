package com.github.marcelldechant.bistro.order.mapper;

import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.orderitem.mapper.OrderItemMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper class for converting between Order entities and OrderResponseDto.
 * This class provides methods to convert an Order entity to a response DTO
 * and to create an Order entity from its components.
 *
 * @author Marcell Dechant
 */
public class OrderMapper {

    /**
     * Private constructor to prevent instantiation of the mapper class.
     * This class is intended to be used statically, so no instances should be created.
     */
    private OrderMapper() {
    }

    /**
     * Converts the provided order details into an Order entity.
     *
     * @param tableNumber the table number associated with the order
     * @param items       the list of order items
     * @param subtotal    the subtotal amount for the order
     * @param discount    the discount applied to the order
     * @param total       the total amount for the order after applying discounts
     * @param isHappyHour indicates if the order is during happy hour
     * @return an Order entity constructed from the provided details
     */
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

    /**
     * Converts an Order entity to an OrderResponseDto.
     *
     * @param order the Order entity to convert
     * @return an OrderResponseDto containing the order details
     */
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

package com.github.marcelldechant.bistro.order.service;

import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.order.exception.DuplicateException;
import com.github.marcelldechant.bistro.order.exception.NoItemsException;
import com.github.marcelldechant.bistro.order.exception.OrderNotFoundException;
import com.github.marcelldechant.bistro.order.exception.QuantityException;
import com.github.marcelldechant.bistro.order.mapper.OrderMapper;
import com.github.marcelldechant.bistro.order.repository.OrderRepository;
import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.orderitem.mapper.OrderItemMapper;
import com.github.marcelldechant.bistro.product.entity.Product;
import com.github.marcelldechant.bistro.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing orders in the Bistro application.
 * This class provides methods to create and retrieve orders, including handling happy hour discounts.
 * It uses OrderRepository for data access and ProductService for product-related operations.
 *
 * @author Marcell Dechant
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int HAPPY_HOUR_START_HOUR = 13;
    private static final int HAPPY_HOUR_END_HOUR = 19;
    private static final double HAPPY_HOUR_DISCOUNT = 0.10;

    private final OrderRepository orderRepository;
    private final ProductService productService;

    /**
     * Creates a new order based on the provided CreateOrderDto.
     * This method builds the order items, calculates the subtotal, discount, and total,
     * and checks if it is happy hour to apply the discount.
     *
     * @param dto the CreateOrderDto containing order details
     * @return an OrderResponseDto containing the created order details
     */
    public OrderResponseDto createOrder(CreateOrderDto dto) {
        List<OrderItem> items = buildOrderItems(dto.items());
        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal discount = calculateDiscount(subtotal);
        BigDecimal total = subtotal.subtract(discount);
        boolean isHappyHour = isHappyHour();

        Order order = OrderMapper.toEntity(dto.tableNumber(), items, subtotal, discount, total, isHappyHour);
        return OrderMapper.toResponseDto(orderRepository.save(order));
    }

    /**
     * Retrieves an order by its ID.
     * This method fetches the order from the repository and maps it to an OrderResponseDto
     *
     * @param id the ID of the order to retrieve
     * @return an OrderResponseDto containing the order details
     * @throws IllegalArgumentException if no order is found with the given ID
     */
    public OrderResponseDto getOrderById(long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toResponseDto)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    /**
     * Retrieves an order entity by its ID.
     * This method fetches the order from the repository without mapping it to a DTO.
     *
     * @param id the ID of the order to retrieve
     * @return the Order entity
     * @throws IllegalArgumentException if no order is found with the given ID
     */
    public Order getOrderByIdEntity(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    /**
     * Builds a list of OrderItem entities from the provided CreateOrderItemDto list.
     * Validates each item to ensure the quantity is greater than 0 and retrieves the corresponding Product entity.
     *
     * @param itemDtos the list of CreateOrderItemDto to convert
     * @return a list of OrderItem entities
     */
    private List<OrderItem> buildOrderItems(List<CreateOrderItemDto> itemDtos) {
        if (itemDtos.isEmpty()) {
            throw new NoItemsException("Order must contain at least one item");
        }

        validateNoDuplicateProducts(itemDtos);

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItemDto dto : itemDtos) {
            if (dto.quantity() <= 0) {
                throw new QuantityException("Quantity must be greater than 0");
            }

            Product product = productService.getProductByIdEntity(dto.productId());
            items.add(OrderItemMapper.toEntity(dto, product));
        }
        return items;
    }

    /**
     * Validates that there are no duplicate products in the order items.
     * If duplicates are found, an IllegalArgumentException is thrown.
     *
     * @param items the list of CreateOrderItemDto to validate
     */
    private void validateNoDuplicateProducts(List<CreateOrderItemDto> items) {
        long uniqueCount = items.stream()
                .map(CreateOrderItemDto::productId)
                .distinct()
                .count();

        if (uniqueCount < items.size()) {
            throw new DuplicateException("Duplicate products in order are not allowed");
        }
    }

    /**
     * Calculates the subtotal of the order by summing up the total prices of all order items.
     *
     * @param items the list of order items
     * @return the subtotal as a BigDecimal
     */
    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Checks if the current time is within the happy hour period.
     * Happy hour is defined as the time between HAPPY_HOUR_START_HOUR and HAPPY_HOUR_END_HOUR.
     *
     * @return true if the current time is within happy hour, false otherwise
     */
    private boolean isHappyHour() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(LocalTime.of(HAPPY_HOUR_START_HOUR, 0)) && now.isBefore(LocalTime.of(HAPPY_HOUR_END_HOUR, 0));
    }

    /**
     * Calculates the discount based on the subtotal.
     * If it is happy hour, a HAPPY_HOUR_DISCOUNT is applied.
     *
     * @param subtotal the subtotal of the order
     * @return the discount amount
     */
    private BigDecimal calculateDiscount(BigDecimal subtotal) {
        return isHappyHour() ? subtotal.multiply(BigDecimal.valueOf(HAPPY_HOUR_DISCOUNT)) : BigDecimal.ZERO;
    }

}

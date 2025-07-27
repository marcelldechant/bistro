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

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final LocalTime HAPPY_HOUR_START = LocalTime.of(17, 0);
    private static final LocalTime HAPPY_HOUR_END = LocalTime.of(21, 0);
    private static final BigDecimal HAPPY_HOUR_DISCOUNT = BigDecimal.valueOf(0.10);

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderResponseDto createOrder(CreateOrderDto dto) {
        List<OrderItem> items = buildOrderItems(dto.items());
        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal discount = calculateDiscount(subtotal);
        BigDecimal total = subtotal.subtract(discount);
        boolean isHappyHour = isHappyHour();

        Order order = OrderMapper.toEntity(dto.tableNumber(), items, subtotal, discount, total, isHappyHour);
        return OrderMapper.toResponseDto(orderRepository.save(order));
    }

    public OrderResponseDto getOrderById(long id) {
        return OrderMapper.toResponseDto(getOrderByIdEntity(id));
    }

    public Order getOrderByIdEntity(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

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

    private void validateNoDuplicateProducts(List<CreateOrderItemDto> items) {
        long uniqueCount = items.stream()
                .map(CreateOrderItemDto::productId)
                .distinct()
                .count();

        if (uniqueCount < items.size()) {
            throw new DuplicateException("Duplicate products in order are not allowed");
        }
    }

    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isHappyHour() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(HAPPY_HOUR_START) && now.isBefore(HAPPY_HOUR_END);
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal) {
        return isHappyHour() ? subtotal.multiply(HAPPY_HOUR_DISCOUNT) : BigDecimal.ZERO;
    }
}

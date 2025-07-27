package com.github.marcelldechant.bistro.order.service;

import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.order.exception.DuplicateException;
import com.github.marcelldechant.bistro.order.exception.NoItemsException;
import com.github.marcelldechant.bistro.order.exception.OrderNotFoundException;
import com.github.marcelldechant.bistro.order.repository.OrderRepository;
import com.github.marcelldechant.bistro.order.util.TimeProvider;
import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.orderitem.mapper.OrderItemMapper;
import com.github.marcelldechant.bistro.product.entity.Product;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import com.github.marcelldechant.bistro.product.repository.ProductRepository;
import com.github.marcelldechant.bistro.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);

    private final ProductService productService = new ProductService(productRepository);
    private final OrderService orderService = new OrderService(orderRepository, productService, timeProvider);

    @Test
    void createOrder_shouldCreateOrderSuccessfully_whenValidInput() {
        CreateOrderItemDto itemDto = new CreateOrderItemDto(1L, 2);
        CreateOrderDto orderDto = new CreateOrderDto(5, List.of(itemDto));

        Product product = Product.builder()
                .id(1L)
                .name("Burger")
                .price(BigDecimal.valueOf(5.00))
                .build();

        Order savedOrder = Order.builder()
                .id(1L)
                .tableNumber(5)
                .items(List.of(OrderItemMapper.toEntity(itemDto, product)))
                .subtotal(BigDecimal.valueOf(10.00))
                .discount(BigDecimal.ZERO)
                .total(BigDecimal.valueOf(10.00))
                .isHappyHour(false)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(savedOrder);
        when(timeProvider.now()).thenReturn(LocalTime.of(17, 0));

        OrderResponseDto result = orderService.createOrder(orderDto);

        assertThat(result)
                .isNotNull()
                .extracting(OrderResponseDto::id, OrderResponseDto::tableNumber, OrderResponseDto::subtotal,
                        OrderResponseDto::discount, OrderResponseDto::total, OrderResponseDto::isHappyHour)
                .containsExactly(1L, 5, BigDecimal.valueOf(10.00), BigDecimal.ZERO, BigDecimal.valueOf(10.00), false);

        assertThat(result.items())
                .hasSize(1)
                .extracting(item -> item.product().getName(), OrderItemResponseDto::quantity)
                .containsExactly(tuple("Burger", 2));
    }

    @Test
    void createOrder_shouldThrowException_whenItemsListIsEmpty() {
        CreateOrderDto dto = new CreateOrderDto(1, List.of());

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(NoItemsException.class)
                .hasMessageContaining("Order must contain at least one item");
    }

    @Test
    void createOrder_shouldThrowException_whenProductListContainsDuplicates() {
        CreateOrderItemDto item1 = new CreateOrderItemDto(1L, 1);
        CreateOrderItemDto item2 = new CreateOrderItemDto(1L, 2);

        CreateOrderDto dto = new CreateOrderDto(3, List.of(item1, item2));

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("Duplicate products in order are not allowed");
    }

    @Test
    void createOrder_shouldThrowException_whenProductIdDoesNotExist() {
        CreateOrderItemDto item = new CreateOrderItemDto(99L, 1);
        CreateOrderDto dto = new CreateOrderDto(4, List.of(item));

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found with id: 99");
    }

    @Test
    void getOrderById_shouldReturnOrderResponseDto_whenOrderExists() {
        long orderId = 1L;
        Product product = new Product(1L, "Cola", BigDecimal.valueOf(2.50));
        OrderItem orderItem = new OrderItem(1L, product, 2, BigDecimal.valueOf(2.50), BigDecimal.valueOf(5.00));

        Order order = Order.builder()
                .id(orderId)
                .tableNumber(7)
                .items(List.of(orderItem))
                .subtotal(BigDecimal.valueOf(5.00))
                .discount(BigDecimal.ZERO)
                .total(BigDecimal.valueOf(5.00))
                .isHappyHour(false)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponseDto response = orderService.getOrderById(orderId);

        assertThat(response)
                .isNotNull()
                .extracting(OrderResponseDto::id, OrderResponseDto::tableNumber, OrderResponseDto::subtotal,
                        OrderResponseDto::discount, OrderResponseDto::total, OrderResponseDto::isHappyHour)
                .containsExactly(orderId, 7, BigDecimal.valueOf(5.00),
                        BigDecimal.ZERO, BigDecimal.valueOf(5.00), false);

        assertThat(response.items())
                .hasSize(1)
                .extracting(item -> item.product().getName(), OrderItemResponseDto::quantity)
                .containsExactly(tuple("Cola", 2));
    }

    @Test
    void getOrderById_shouldThrowException_whenOrderDoesNotExist() {
        long nonExistentId = 999L;

        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(nonExistentId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found with id: " + nonExistentId);
    }

    @Test
    void getOrderByIdEntity_shouldReturnOrder_whenOrderExists() {
        long orderId = 1L;
        Product product = new Product(1L, "Pizza", BigDecimal.valueOf(7.50));
        OrderItem orderItem = new OrderItem(1L, product, 1, BigDecimal.valueOf(7.50), BigDecimal.valueOf(7.50));

        Order order = Order.builder()
                .id(orderId)
                .tableNumber(2)
                .items(List.of(orderItem))
                .subtotal(BigDecimal.valueOf(7.50))
                .discount(BigDecimal.ZERO)
                .total(BigDecimal.valueOf(7.50))
                .isHappyHour(false)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderByIdEntity(orderId);

        assertThat(result)
                .isNotNull()
                .extracting(Order::getId, Order::getTableNumber, Order::getSubtotal,
                        Order::getDiscount, Order::getTotal, Order::isHappyHour)
                .containsExactly(orderId, 2, BigDecimal.valueOf(7.50),
                        BigDecimal.ZERO, BigDecimal.valueOf(7.50), false);

        assertThat(result.getItems())
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    assertThat(item.getProduct().getName()).isEqualTo("Pizza");
                    assertThat(item.getQuantity()).isEqualTo(1);
                });
    }

    @Test
    void getOrderByIdEntity_shouldThrowException_whenOrderDoesNotExist() {
        long missingId = 999L;

        when(orderRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderByIdEntity(missingId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found with id: " + missingId);
    }
}
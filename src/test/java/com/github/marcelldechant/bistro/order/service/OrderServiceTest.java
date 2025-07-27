package com.github.marcelldechant.bistro.order.service;

import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.order.exception.DuplicateException;
import com.github.marcelldechant.bistro.order.exception.NoItemsException;
import com.github.marcelldechant.bistro.order.exception.OrderNotFoundException;
import com.github.marcelldechant.bistro.order.exception.QuantityException;
import com.github.marcelldechant.bistro.order.repository.OrderRepository;
import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.product.entity.Product;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import com.github.marcelldechant.bistro.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void cleanDatabase() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void createOrder_shouldPersistCorrectly_whenValidRequestProvided() {
        Product cola = createProduct("Cola", BigDecimal.valueOf(2.50));
        Product pizza = createProduct("Pizza", BigDecimal.valueOf(10.00));

        CreateOrderDto dto = new CreateOrderDto(
                5,
                List.of(
                        new CreateOrderItemDto(pizza.getId(), 2),
                        new CreateOrderItemDto(cola.getId(), 1)
                )
        );

        OrderResponseDto response = orderService.createOrder(dto);

        assertThat(response.tableNumber()).isEqualTo(5);
        assertThat(response.orderItems()).hasSize(2);
        assertThat(response.subtotal()).isEqualByComparingTo("22.50");
        assertThat(response.total()).isEqualByComparingTo(response.subtotal().subtract(response.discount()));
    }

    @Test
    void createOrder_shouldThrowQuantityException_whenInvalidQuantityProvided() {
        Product pizza = createProduct("Pizza", BigDecimal.valueOf(10.00));
        int invalidQuantity = -1;

        CreateOrderDto dto = new CreateOrderDto(
                5,
                List.of(new CreateOrderItemDto(pizza.getId(), invalidQuantity))
        );

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(QuantityException.class)
                .hasMessageContaining("Quantity must be greater than 0");
    }

    @Test
    void createOrder_shouldThrowException_whenProductIdDoesNotExist() {
        long nonExistentProductId = 999L;

        CreateOrderDto dto = new CreateOrderDto(
                10,
                List.of(new CreateOrderItemDto(nonExistentProductId, 1))
        );

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found with id");
    }

    @Test
    void createOrder_shouldThrowException_whenItemsListIsEmpty() {
        CreateOrderDto dto = new CreateOrderDto(
                5,
                List.of()
        );

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(NoItemsException.class)
                .hasMessageContaining("Order must contain at least one item");
    }

    @Test
    void createOrder_shouldThrowException_whenProductListContainsDuplicates() {
        Product cola = createProduct("Cola", BigDecimal.valueOf(2.50));

        CreateOrderDto dto = new CreateOrderDto(
                10,
                List.of(
                        new CreateOrderItemDto(cola.getId(), 1),
                        new CreateOrderItemDto(cola.getId(), 2)
                )
        );

        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("Duplicate products in order are not allowed");
    }

    @Test
    void getOrderById_shouldReturnCorrectOrder_whenValidIdProvided() {
        Product pizza = createProduct("Pizza", BigDecimal.valueOf(10.00));
        CreateOrderDto dto = new CreateOrderDto(
                5,
                List.of(new CreateOrderItemDto(pizza.getId(), 1))
        );

        OrderResponseDto createdOrder = orderService.createOrder(dto);
        OrderResponseDto fetchedOrder = orderService.getOrderById(createdOrder.id());

        assertThat(fetchedOrder.id()).isEqualTo(createdOrder.id());
        assertThat(fetchedOrder.tableNumber()).isEqualTo(createdOrder.tableNumber());
        assertThat(fetchedOrder.orderItems()).hasSize(1);
    }

    @Test
    void getOrderById_shouldThrowException_whenOrderDoesNotExist() {
        long nonExistentOrderId = 999L;
        assertThatThrownBy(() -> orderService.getOrderById(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found with id: " + nonExistentOrderId);
    }

    @Test
    void getOrderByIdEntity_shouldReturnCorrectOrderEntity_whenValidIdProvided() {
        Product pizza = createProduct("Pizza", BigDecimal.valueOf(10.00));
        CreateOrderDto dto = new CreateOrderDto(
                5,
                List.of(new CreateOrderItemDto(pizza.getId(), 1))
        );

        OrderResponseDto createdOrder = orderService.createOrder(dto);
        Order fetchedOrder = orderService.getOrderByIdEntity(createdOrder.id());

        assertThat(fetchedOrder.getId()).isEqualTo(createdOrder.id());
        assertThat(fetchedOrder.getTableNumber()).isEqualTo(createdOrder.tableNumber());
        assertThat(fetchedOrder.getItems()).hasSize(1);
    }

    @Test
    void getOrderByIdEntity_shouldThrowException_whenOrderDoesNotExist() {
        long nonExistentOrderId = 999L;
        assertThatThrownBy(() -> orderService.getOrderByIdEntity(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found with id: " + nonExistentOrderId);
    }

    private Product createProduct(String name, BigDecimal price) {
        return productRepository.save(Product.builder().name(name).price(price).build());
    }
}
package com.github.marcelldechant.bistro.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marcelldechant.bistro.exception.dto.CustomApiErrorResponseDto;
import com.github.marcelldechant.bistro.order.config.OrderServiceTestConfig;
import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.order.exception.DuplicateException;
import com.github.marcelldechant.bistro.order.exception.NoItemsException;
import com.github.marcelldechant.bistro.order.exception.OrderNotFoundException;
import com.github.marcelldechant.bistro.order.exception.QuantityException;
import com.github.marcelldechant.bistro.order.mapper.OrderMapper;
import com.github.marcelldechant.bistro.order.service.OrderService;
import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;
import com.github.marcelldechant.bistro.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToCompressingWhiteSpace;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(OrderServiceTestConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetMock() {
        reset(orderService);
    }

    @Test
    void createOrder_returnsCreatedAndOrder() throws Exception {
        CreateOrderDto createOrderDto = new CreateOrderDto(10, List.of(
                new CreateOrderItemDto(1L, 2),
                new CreateOrderItemDto(2L, 1)
        ));

        Order mockOrder = new Order(1L, 10, List.of(
                new OrderItem(1L, new Product(1L, "Pizza", BigDecimal.valueOf(8.50)), 2, BigDecimal.valueOf(8.50), BigDecimal.valueOf(17.00)),
                new OrderItem(2L, new Product(2L, "Cola", BigDecimal.valueOf(2.50)), 1, BigDecimal.valueOf(2.50), BigDecimal.valueOf(2.50))
        ), BigDecimal.valueOf(19.50), BigDecimal.ZERO, BigDecimal.valueOf(19.50), false);
        OrderResponseDto expectedResponse = OrderMapper.toResponseDto(mockOrder);

        when(orderService.createOrder(createOrderDto)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void createOrder_returnsBadRequest_whenWrongQuantityProvied() throws Exception {
        CreateOrderDto createOrderDto = new CreateOrderDto(10, List.of(
                new CreateOrderItemDto(1L, 2),
                new CreateOrderItemDto(2L, 1)
        ));

        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                "Quantity must be greater than 0",
                "/api/v1/orders",
                null,
                400
        );

        when(orderService.createOrder(createOrderDto)).thenThrow(new QuantityException("Quantity must be greater than 0"));

        String result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(result, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

    @Test
    void createOrder_returnsBadRequest_whenNoItemsProvided() throws Exception {
        CreateOrderDto createOrderDto = new CreateOrderDto(10, List.of());

        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                "Order must contain at least one item",
                "/api/v1/orders",
                null,
                400
        );

        when(orderService.createOrder(createOrderDto)).thenThrow(new NoItemsException("Order must contain at least one item"));

        String result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(result, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

    @Test
    void createOrder_returnsConflict_whenAtLeastTwoItemsHaveTheSameProductId() throws Exception {
        CreateOrderDto createOrderDto = new CreateOrderDto(10, List.of(
                new CreateOrderItemDto(1L, 2),
                new CreateOrderItemDto(1L, 1)
        ));

        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                "Duplicate products in order are not allowed",
                "/api/v1/orders",
                null,
                409
        );

        when(orderService.createOrder(createOrderDto)).thenThrow(new DuplicateException("Duplicate products in order are not allowed"));

        String result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(result, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

    @Test
    void getOrderById_returnsOkAndOrder() throws Exception {
        Order mockOrder = new Order(1L, 10, List.of(
                new OrderItem(1L, new Product(1L, "Pizza", BigDecimal.valueOf(8.50)), 2, BigDecimal.valueOf(8.50), BigDecimal.valueOf(17.00)),
                new OrderItem(2L, new Product(2L, "Cola", BigDecimal.valueOf(2.50)), 1, BigDecimal.valueOf(2.50), BigDecimal.valueOf(2.50))
        ), BigDecimal.valueOf(19.50), BigDecimal.ZERO, BigDecimal.valueOf(19.50), false);
        OrderResponseDto expectedResponse = OrderMapper.toResponseDto(mockOrder);

        when(orderService.getOrderById(1L)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void getOrderById_returnsNotFound_whenOrderDoesNotExist() throws Exception {
        long notExistingOrderId = 999L;
        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                "Order not found with id: " + notExistingOrderId,
                "/api/v1/orders/" + notExistingOrderId,
                null,
                404
        );

        when(orderService.getOrderById(notExistingOrderId)).thenThrow(new OrderNotFoundException("Order not found with id: " + notExistingOrderId));

        String result = mockMvc.perform(get("/api/v1/orders/" + notExistingOrderId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(result, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

    @Test
    void getReceiptByOrderId_returnsOkAndReceiptWithoutDiscount() throws Exception {
        Order mockOrder = new Order(1L, 10, List.of(
                new OrderItem(1L, new Product(1L, "Pizza", BigDecimal.valueOf(8.50)), 2, BigDecimal.valueOf(8.50), BigDecimal.valueOf(17.00)),
                new OrderItem(2L, new Product(2L, "Cola", BigDecimal.valueOf(2.50)), 1, BigDecimal.valueOf(2.50), BigDecimal.valueOf(2.50))
        ), BigDecimal.valueOf(19.50), BigDecimal.ZERO, BigDecimal.valueOf(19.50), false);

        String expectedReceipt = """
                -------------------------
                Table Nr. 10
                -------------------------
                2 x Pizza @ 8,50 = 17,00
                1 x Cola @ 2,50 = 2,50
                -------------------------
                Subtotal: 19,50
                Total: 19,50
                """;

        when(orderService.getOrderByIdEntity(1L)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/v1/orders/1/receipt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(result -> {
                    String actual = result.getResponse().getContentAsString();
                    assertThat(actual, equalToCompressingWhiteSpace(expectedReceipt));
                });
    }

    @Test
    void getReceiptByOrderId_returnsOkAndReceiptWithDiscount() throws Exception {
        Order mockOrder = new Order(1L, 10, List.of(
                new OrderItem(1L, new Product(1L, "Pizza", BigDecimal.valueOf(8.50)), 2, BigDecimal.valueOf(8.50), BigDecimal.valueOf(17.00)),
                new OrderItem(2L, new Product(2L, "Cola", BigDecimal.valueOf(2.50)), 1, BigDecimal.valueOf(2.50), BigDecimal.valueOf(2.50))
        ), BigDecimal.valueOf(19.50), BigDecimal.valueOf(1.95), BigDecimal.valueOf(19.50), true);

        String expectedReceipt = """
                -------------------------
                Table Nr. 10
                -------------------------
                2 x Pizza @ 8,50 = 17,00
                1 x Cola @ 2,50 = 2,50
                -------------------------
                Subtotal: 19,50
                Discount: 10%
                Total: 19,50
                """;

        when(orderService.getOrderByIdEntity(1L)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/v1/orders/1/receipt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(result -> {
                    String actual = result.getResponse().getContentAsString();
                    assertThat(actual, equalToCompressingWhiteSpace(expectedReceipt));
                });
    }

    @Test
    void getReceiptByOrderId_returnsNotFound_whenOrderDoesNotExist() throws Exception {
        long notExistingOrderId = 999L;
        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                "Order not found with id: " + notExistingOrderId,
                "/api/v1/orders/" + notExistingOrderId + "/receipt",
                null,
                404
        );

        when(orderService.getOrderByIdEntity(notExistingOrderId)).thenThrow(new OrderNotFoundException("Order not found with id: " + notExistingOrderId));

        String result = mockMvc.perform(get("/api/v1/orders/" + notExistingOrderId + "/receipt"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(result, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

}

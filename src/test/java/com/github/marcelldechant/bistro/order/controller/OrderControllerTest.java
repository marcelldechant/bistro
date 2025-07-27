package com.github.marcelldechant.bistro.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.orderitem.dto.CreateOrderItemDto;
import com.github.marcelldechant.bistro.orderitem.dto.OrderItemResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /*
     * Set the default locale to Germany for consistent number formatting in tests.
     */
    @BeforeAll
    static void setLocale() {
        Locale.setDefault(Locale.GERMANY);
    }

    @Test
    void createOrder_returnsCreatedOrder_whenValidInput() throws Exception {
        CreateOrderDto requestDto = new CreateOrderDto(
                3,
                List.of(new CreateOrderItemDto(1L, 2))
        );

        String requestBody = objectMapper.writeValueAsString(requestDto);

        String responseJson = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponseDto response = objectMapper.readValue(responseJson, OrderResponseDto.class);

        assertThat(response)
                .isNotNull()
                .extracting(OrderResponseDto::id, OrderResponseDto::tableNumber, OrderResponseDto::isHappyHour)
                .containsExactly(1L, 3, false);

        assertThat(response.items())
                .hasSize(1)
                .extracting(item -> item.product().getName(), OrderItemResponseDto::quantity)
                .containsExactlyInAnyOrder(
                        tuple("cola", 2)
                );

        assertThat(response.total().doubleValue()).isEqualTo(5.00);
    }

    @Test
    void createOrder_returnsBadRequest_whenItemsAreEmpty() throws Exception {
        CreateOrderDto requestDto = new CreateOrderDto(2, List.of());

        String requestBody = objectMapper.writeValueAsString(requestDto);

        String responseJson = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseJson).contains("Order must contain at least one item");
    }

    @Test
    void createOrder_returnsBadRequest_whenQuantityIsZero() throws Exception {
        CreateOrderDto requestDto = new CreateOrderDto(
                4,
                List.of(new CreateOrderItemDto(1L, 0))
        );

        String requestBody = objectMapper.writeValueAsString(requestDto);

        String responseJson = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseJson).contains("Quantity must be greater than 0");
    }

    @Test
    void createOrder_returnsBadRequest_whenProductIdsAreDuplicate() throws Exception {
        CreateOrderDto requestDto = new CreateOrderDto(
                5,
                List.of(
                        new CreateOrderItemDto(2L, 1),
                        new CreateOrderItemDto(2L, 3)
                )
        );

        String requestBody = objectMapper.writeValueAsString(requestDto);

        String responseJson = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseJson).contains("Duplicate products in order are not allowed");
    }

    @Test
    void getOrderById_returnsOrder_whenOrderExists() throws Exception {
        CreateOrderDto createDto = new CreateOrderDto(
                6,
                List.of(new CreateOrderItemDto(1L, 2))
        );

        String createJson = objectMapper.writeValueAsString(createDto);

        String createdOrderJson = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponseDto createdOrder = objectMapper.readValue(createdOrderJson, OrderResponseDto.class);
        Long createdId = createdOrder.id();

        String getResponseJson = mockMvc.perform(get("/api/v1/orders/" + createdId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponseDto fetchedOrder = objectMapper.readValue(getResponseJson, OrderResponseDto.class);

        assertThat(fetchedOrder)
                .isNotNull()
                .extracting(OrderResponseDto::id, OrderResponseDto::tableNumber)
                .containsExactly(createdId, 6);

        assertThat(fetchedOrder.items()).hasSize(1);
        assertThat(fetchedOrder.items().getFirst().product().getName()).isEqualTo("cola");
    }

    @Test
    void getOrderById_returnsNotFound_whenOrderDoesNotExist() throws Exception {
        long nonExistentId = 9999L;

        String responseJson = mockMvc.perform(get("/api/v1/orders/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseJson).contains("Order not found with id: " + nonExistentId);
    }

    @Test
    void getReceiptByOrderId_returnsFormattedReceipt_whenOrderExists() throws Exception {
        CreateOrderDto createDto = new CreateOrderDto(
                10,
                List.of(
                        new CreateOrderItemDto(1L, 2),
                        new CreateOrderItemDto(2L, 1),
                        new CreateOrderItemDto(3L, 1)
                )
        );

        String createJson = objectMapper.writeValueAsString(createDto);

        String createdOrderJson = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponseDto createdOrder = objectMapper.readValue(createdOrderJson, OrderResponseDto.class);
        long orderId = createdOrder.id();

        String receipt = mockMvc.perform(get("/api/v1/orders/" + orderId + "/receipt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> expectedLines = List.of(
                "-------------------------",
                "Table Nr. 10",
                "-------------------------",
                "2 x cola @ 2,50 = 5,00",
                "1 x pizza @ 6,00 = 6,00",
                "1 x burger @ 7,00 = 7,00",
                "-------------------------",
                "Subtotal: 18,00",
                "Total: 18,00"
        );

        List<String> actualLines = receipt.lines()
                .map(String::strip)
                .toList();

        assertThat(actualLines).containsExactlyElementsOf(expectedLines);
    }

    @Test
    void getReceiptByOrderId_returnsNotFound_whenOrderDoesNotExist() throws Exception {
        long nonExistentId = 9999L;

        String response = mockMvc.perform(get("/api/v1/orders/" + nonExistentId + "/receipt"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains("Order not found with id: " + nonExistentId);
    }
}

package com.github.marcelldechant.bistro.order.controller;

import com.github.marcelldechant.bistro.order.api.OrderApi;
import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.order.service.OrderService;
import com.github.marcelldechant.bistro.order.util.ReceiptFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing orders in the Bistro application.
 * This class handles HTTP requests related to orders and delegates the business logic to the OrderService.
 * It is annotated with @RestController to indicate that it is a RESTful controller.
 * The base URL for this controller is "/api/v1/orders".
 *
 * @author Marcell Dechant
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    /**
     * Creates a new order.
     * This method is mapped to the POST request for the base URL of this controller.
     *
     * @param createOrderDto the DTO containing the order details
     * @return an OrderResponseDto containing the created order details
     */
    @Override
    public OrderResponseDto createOrder(CreateOrderDto createOrderDto) {
        return orderService.createOrder(createOrderDto);
    }

    /**
     * Retrieves an order by its ID.
     * This method is mapped to the GET request for the URL "/{id}" where {id} is the order ID.
     *
     * @param id the ID of the order to retrieve
     * @return an OrderResponseDto containing the order details
     */
    @Override
    public OrderResponseDto getOrderById(@PathVariable long id) {
        return orderService.getOrderById(id);
    }

    /**
     * Retrieves the receipt for an order by its ID.
     * This method is mapped to the GET request for the URL "/{id}/receipt" where {id} is the order ID.
     *
     * @param id the ID of the order for which to retrieve the receipt
     * @return a String containing the formatted receipt
     */
    @Override
    public String getReceipt(@PathVariable long id) {
        Order order = orderService.getOrderByIdEntity(id);
        return ReceiptFormatter.format(order);
    }

}

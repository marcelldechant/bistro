package com.github.marcelldechant.bistro.order.api;

import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * API interface for managing orders in the Bistro application.
 * This interface defines the endpoints for retrieving orders information.
 * It is used to generate OpenAPI documentation for the orders-related operations.
 *
 * @author Marcell Dechant
 */
public interface OrderApi {

    /**
     * Creates a new order based on the provided CreateOrderDto.
     * This method is mapped to the POST request for the base URL of this controller.
     *
     * @param createOrderDto the DTO containing order details to be created
     * @return an OrderResponseDto containing the created order details
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    //TODO: Add OpenAPI documentation annotations
    OrderResponseDto createOrder(@RequestBody CreateOrderDto createOrderDto);

    /**
     * Retrieves an order by its ID from the order service.
     * This method is mapped to the GET request for the URL "/{id}" where {id} is the order ID.
     *
     * @param id the ID of the order to retrieve
     * @return an OrderResponseDto containing the order details
     */
    //TODO: Add OpenAPI documentation annotations
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    OrderResponseDto getOrderById(@PathVariable long id);

    /**
     * Retrieves the receipt for an order by its ID.
     * This method is mapped to the GET request for the URL "/{id}/receipt" where {id} is the order ID.
     *
     * @param id the ID of the order for which to retrieve the receipt
     * @return a String containing the receipt details
     */
    //TODO: Add OpenAPI documentation annotations
    @GetMapping(value = "/{id}/receipt", produces = MediaType.TEXT_PLAIN_VALUE)
    String getReceipt(@PathVariable long id);
}

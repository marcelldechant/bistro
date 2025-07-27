package com.github.marcelldechant.bistro.order.api;

import com.github.marcelldechant.bistro.exception.dto.CustomApiErrorResponseDto;
import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with the provided details",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "id": 1,
                                                "tableNumber": 5,
                                                "orderItems": [
                                                    {
                                                    "productId": 1,
                                                    "quantity": 2,
                                                    "name": "Pizza Margherita",
                                                    "price": 8.50
                                                    },
                                                    {
                                                    "productId": 2,
                                                    "quantity": 1,
                                                    "name": "Cola",
                                                    "price": 2.50
                                                    }
                                                ],
                                                "subtotal": 19.50,
                                                "discount": 0.00,
                                                "total": 19.50,
                                                "isHappyHour": false
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data (e.g. quantity <= 0, empty item list, duplicates)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Quantity must be greater than 0",
                                              "path": "/api/v1/orders",
                                              "timestamp": "2025-07-27T12:00:00Z",
                                              "status": 400
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Product not found with id: 99",
                                              "path": "/api/v1/orders",
                                              "timestamp": "2025-07-27T12:01:00Z",
                                              "status": 404
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Duplicate products in order",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Duplicate products in order are not allowed",
                                              "path": "/api/v1/orders",
                                              "timestamp": "2025-07-27T12:01:00Z",
                                              "status": 409
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Database connection failed",
                                              "path": "/api/v1/orders",
                                              "timestamp": "2025-07-25T22:18:45.123Z",
                                              "statusCode": 500
                                            }
                                            """)
                            )
                    )
            })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto createOrder(@RequestBody CreateOrderDto createOrderDto);

    /**
     * Retrieves an order by its ID from the order service.
     * This method is mapped to the GET request for the URL "/{id}" where {id} is the order ID.
     *
     * @param id the ID of the order to retrieve
     * @return an OrderResponseDto containing the order details
     */
    @Operation(
            summary = "Retrieve an order by ID",
            description = "Fetches an order by its unique ID. Returns 404 if the order does not exist.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Pizza and Cola",
                                                    summary = "Simple Order Example",
                                                    value = """
                                                            {
                                                              "id": 1,
                                                              "tableNumber": 3,
                                                              "orderItems": [
                                                                {
                                                                  "productId": 1,
                                                                  "quantity": 2,
                                                                  "name": "Pizza Margherita",
                                                                  "price": 8.50
                                                                },
                                                                {
                                                                  "productId": 2,
                                                                  "quantity": 1,
                                                                  "name": "Cola",
                                                                  "price": 2.50
                                                                }
                                                              ],
                                                              "subtotal": 19.50,
                                                              "discount": 0.00,
                                                              "total": 19.50,
                                                              "isHappyHour": false
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Happy Hour Order",
                                                    summary = "Order with discount",
                                                    value = """
                                                            {
                                                              "id": 2,
                                                              "tableNumber": 7,
                                                              "orderItems": [
                                                                {
                                                                  "productId": 3,
                                                                  "quantity": 1,
                                                                  "name": "Burger",
                                                                  "price": 7.00
                                                                }
                                                              ],
                                                              "subtotal": 7.00,
                                                              "discount": 0.70,
                                                              "total": 6.30,
                                                              "isHappyHour": true
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid order ID format (e.g., non-numeric)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Failed to convert value of type 'java.lang.String' to required type 'long'",
                                              "path": "/api/v1/orders/abc",
                                              "timestamp": "2025-07-27T12:15:00Z",
                                              "status": 400
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Order not found with id: 99",
                                              "path": "/api/v1/orders/99",
                                              "timestamp": "2025-07-27T12:05:00Z",
                                              "status": 404
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Database connection failed",
                                              "path": "/api/v1/orders/{id}",
                                              "timestamp": "2025-07-25T22:18:45.123Z",
                                              "statusCode": 500
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    OrderResponseDto getOrderById(@PathVariable long id);

    /**
     * Retrieves the receipt for an order by its ID.
     * This method is mapped to the GET request for the URL "/{id}/receipt" where {id} is the order ID.
     *
     * @param id the ID of the order for which to retrieve the receipt
     * @return a String containing the receipt details
     */
    @Operation(
            summary = "Retrieve receipt for a given order",
            description = "Generates and returns a plain-text receipt for the specified order ID. Returns 404 if the order does not exist.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Receipt generated successfully without discount",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    name = "Pizza and Cola",
                                                    summary = "Simple Order Example",
                                                    value = """
                                                            -------------------------
                                                            Table Nr. 10
                                                            -------------------------
                                                            2 x cola @ 2,00 = 4,00
                                                            1 x pizza @ 6,00 = 6,00
                                                            -------------------------
                                                            Subtotal: 10,00
                                                            Total: 10,00
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Happy Hour Order",
                                                    summary = "Order with discount",
                                                    value = """
                                                            -------------------------
                                                             Table Nr. 10
                                                             -------------------------
                                                             2 x cola @ 2,00 = 4,00
                                                             1 x pizza @ 6,00 = 6,00
                                                             1 x burger @ 7,00 = 7,00
                                                             -------------------------
                                                             Subtotal: 17,00
                                                             Discount: 10%
                                                             Total: 15,30
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Order not found with id: 99",
                                              "path": "/api/v1/orders/99/receipt",
                                              "timestamp": "2025-07-27T12:10:00Z",
                                              "status": 404
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Database connection failed",
                                              "path": "/api/v1/orders/{id}",
                                              "timestamp": "2025-07-25T22:18:45.123Z",
                                              "statusCode": 500
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping(value = "/{id}/receipt", produces = MediaType.TEXT_PLAIN_VALUE)
    String getReceiptByOrderId(@PathVariable long id);
}

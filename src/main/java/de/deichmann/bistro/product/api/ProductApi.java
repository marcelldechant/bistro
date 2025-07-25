package de.deichmann.bistro.product.api;

import de.deichmann.bistro.exception.dto.CustomApiErrorResponseDto;
import de.deichmann.bistro.product.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * API interface for managing products in the Bistro application.
 * This interface defines the endpoints for retrieving product information.
 * It is used to generate OpenAPI documentation for the product-related operations.
 *
 * @author Marcell Dechant
 */
public interface ProductApi {

    /**
     * Retrieves all products available in the Bistro application.
     * This method returns a list of ProductResponseDto objects containing product details.
     *
     * @return a list of ProductResponseDto representing all products
     */
    @Operation(
            summary = "Get all products",
            description = "Returns a list of all available products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)),
                                    examples = @ExampleObject(value = """
                                            [
                                              {
                                                "id": 1,
                                                "name": "Pizza Margherita",
                                                "price": 8.50
                                              },
                                              {
                                                "id": 2,
                                                "name": "Cola",
                                                "price": 2.50
                                              }
                                            ]
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomApiErrorResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Database connection failed",
                                              "path": "/api/v1/products",
                                              "timestamp": "2025-07-25T22:18:45.123Z",
                                              "statusCode": 500
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping
    List<ProductResponseDto> getAllProducts();

}

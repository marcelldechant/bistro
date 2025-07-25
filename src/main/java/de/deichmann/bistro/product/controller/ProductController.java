package de.deichmann.bistro.product.controller;

import de.deichmann.bistro.product.api.ProductApi;
import de.deichmann.bistro.product.dto.ProductResponseDto;
import de.deichmann.bistro.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for managing products in the Bistro application.
 * This class handles HTTP requests related to products and delegates the business logic to the ProductService.
 * It is annotated with @RestController to indicate that it is a RESTful controller.
 * The base URL for this controller is "/api/v1/products".
 *
 * @author Marcell Dechant
 */
@RestController
@RequestMapping(value = "/api/v1/products", produces = "application/json")
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    /**
     * Retrieves all products from the product service.
     * This method is mapped to the GET request for the base URL of this controller.
     *
     * @return a list of ProductResponseDto containing all products
     */
    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

}

package de.deichmann.bistro.product.service;

import de.deichmann.bistro.product.dto.CreateProductDto;
import de.deichmann.bistro.product.dto.ProductResponseDto;
import de.deichmann.bistro.product.entity.Product;
import de.deichmann.bistro.product.exception.ProductNotFoundException;
import de.deichmann.bistro.product.mapper.ProductMapper;
import de.deichmann.bistro.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service class for managing products in the Bistro application.
 * This class provides methods to interact with the ProductRepository.
 *
 * @author Marcell Dechant
 */
@Service
@RequiredArgsConstructor
@Validated
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Gets all products from the repository.
     * This method retrieves all products and maps them to ProductResponseDto objects.
     *
     * @return a list of ProductResponseDto containing all products
     */
    public List<ProductResponseDto> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductMapper::toResponseDto)
                .toList();
    }

    /**
     * Gets a product by its ID.
     * This method retrieves a product from the repository by its ID and maps it to a ProductResponseDto.
     *
     * @param id the ID of the product to retrieve
     * @return a ProductResponseDto containing the product details
     * @throws ProductNotFoundException if no product is found with the given ID
     */
    public ProductResponseDto getProductById(long id) {
        return productRepository
                .findById(id)
                .map(ProductMapper::toResponseDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    /**
     * Creates a new product.
     * This method saves a new product to the repository using the provided CreateProductDto.
     *
     * @param createProductDto the DTO containing the product details to create
     * @return a ProductResponseDto containing the created product details
     */
    public ProductResponseDto createProduct(CreateProductDto createProductDto) {
        Product savedProduct = productRepository.save(ProductMapper.toEntity(createProductDto));
        return ProductMapper.toResponseDto(savedProduct);
    }

}

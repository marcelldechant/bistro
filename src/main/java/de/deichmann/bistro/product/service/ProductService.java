package de.deichmann.bistro.product.service;

import de.deichmann.bistro.product.dto.ProductResponseDto;
import de.deichmann.bistro.product.mapper.ProductMapper;
import de.deichmann.bistro.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing products in the Bistro application.
 * This class provides methods to interact with the ProductRepository.
 *
 * @author Marcell Dechant
 */
@Service
@RequiredArgsConstructor
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

}

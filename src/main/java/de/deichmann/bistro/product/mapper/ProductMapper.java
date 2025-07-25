package de.deichmann.bistro.product.mapper;

import de.deichmann.bistro.product.dto.CreateProductDto;
import de.deichmann.bistro.product.dto.ProductResponseDto;
import de.deichmann.bistro.product.entity.Product;

/**
 * Mapper class for converting Product entities.
 * It is designed to be used in the service layer where Product entities are handled.
 *
 * @author Marcell Dechant
 */
public class ProductMapper {

    private ProductMapper() {
    }

    /**
     * Converts a Product entity to a ProductResponseDto.
     *
     * @param product the Product entity to convert
     * @return a ProductResponseDto containing the product's id, name, and price
     */
    public static ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice());
    }

    /**
     * Converts a CreateProductDto to a Product entity.
     *
     * @param dto createProductDto to convert
     * @return a Product entity with the name and price from the dto
     */
    public static Product toEntity(CreateProductDto dto) {
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .build();
    }

}

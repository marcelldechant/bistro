package com.github.marcelldechant.bistro.product.mapper;

import com.github.marcelldechant.bistro.product.dto.CreateProductDto;
import com.github.marcelldechant.bistro.product.dto.ProductResponseDto;
import com.github.marcelldechant.bistro.product.entity.Product;

public class ProductMapper {
    private ProductMapper() {
    }

    public static ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice());
    }

    public static Product toEntity(CreateProductDto dto) {
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .build();
    }
}

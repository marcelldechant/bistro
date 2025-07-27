package com.github.marcelldechant.bistro.product.service;

import com.github.marcelldechant.bistro.product.dto.CreateProductDto;
import com.github.marcelldechant.bistro.product.dto.ProductResponseDto;
import com.github.marcelldechant.bistro.product.entity.Product;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import com.github.marcelldechant.bistro.product.mapper.ProductMapper;
import com.github.marcelldechant.bistro.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductMapper::toResponseDto)
                .toList();
    }

    public ProductResponseDto getProductById(long id) {
        return ProductMapper.toResponseDto(getProductByIdEntity(id));
    }

    public Product getProductByIdEntity(long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public ProductResponseDto createProduct(CreateProductDto createProductDto) {
        Product savedProduct = productRepository.save(ProductMapper.toEntity(createProductDto));
        return ProductMapper.toResponseDto(savedProduct);
    }
}

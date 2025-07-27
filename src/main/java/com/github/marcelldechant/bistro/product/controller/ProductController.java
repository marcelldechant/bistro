package com.github.marcelldechant.bistro.product.controller;

import com.github.marcelldechant.bistro.product.api.ProductApi;
import com.github.marcelldechant.bistro.product.dto.ProductResponseDto;
import com.github.marcelldechant.bistro.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/products", produces = "application/json")
@RequiredArgsConstructor
public class ProductController implements ProductApi {
    private final ProductService productService;

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public ProductResponseDto getProductById(long id) {
        return productService.getProductById(id);
    }
}

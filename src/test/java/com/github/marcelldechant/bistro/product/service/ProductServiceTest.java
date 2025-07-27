package com.github.marcelldechant.bistro.product.service;

import com.github.marcelldechant.bistro.product.dto.CreateProductDto;
import com.github.marcelldechant.bistro.product.dto.ProductResponseDto;
import com.github.marcelldechant.bistro.product.entity.Product;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import com.github.marcelldechant.bistro.product.repository.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ProductServiceTest {
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);

    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
    }

    @Test
    void getAllProducts_expectEmptyList_whenNoProductsInDb() {
        List<ProductResponseDto> products = productService.getAllProducts();

        assertThat(products)
                .isEmpty();
    }

    @Test
    void getAllProducts_expectNonEmptyList_whenProductsExistInDb() {
        List<Product> expected = List.of(
                new Product(1L, "Coffee", BigDecimal.valueOf(2.50)),
                new Product(2L, "Tea", BigDecimal.valueOf(1.50)),
                new Product(3L, "Juice", BigDecimal.valueOf(3.00))
        );

        when(productRepository.findAll()).thenReturn(expected);
        List<ProductResponseDto> actual = productService.getAllProducts();

        assertThat(actual)
                .isNotEmpty()
                .hasSize(expected.size())
                .allSatisfy(product -> {
                    assertThat(product.id()).isNotNull();
                    assertThat(product.name()).isNotBlank();
                    assertThat(product.price()).isNotNull();
                });
    }

    @Test
    void getAllProducts_shouldNotFail_withLargeDataset() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            products.add(new Product((long) i, "Product " + i, BigDecimal.valueOf(i * 1.00)));
        }

        when(productRepository.findAll()).thenReturn(products);
        List<ProductResponseDto> actualProducts = productService.getAllProducts();

        assertThat(actualProducts)
                .isNotEmpty()
                .hasSize(1000)
                .allSatisfy(product -> {
                    assertThat(product.id()).isNotNull();
                    assertThat(product.name()).isNotBlank();
                    assertThat(product.price()).isNotNull();
                });
    }

    @Test
    void getProductById_expectProductResponseDto_whenProductExistsInDb() {
        long id = 1L;
        Product expected = Product.builder()
                .id(id)
                .name("Test Product")
                .price(BigDecimal.valueOf(10.00))
                .build();

        when(productRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        ProductResponseDto actual = productService.getProductById(id);

        assertThat(actual)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(expected.getId());
                    assertThat(dto.name()).isEqualTo(expected.getName());
                    assertThat(dto.price()).isEqualByComparingTo(expected.getPrice());
                });
    }

    @Test
    void getProductById_expectProductNotFoundException_whenProductDoesNotExistInDb() {
        long nonExistentId = 999L;

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(nonExistentId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found with id: " + nonExistentId);
    }

    @Test
    void getProductByIdEntity_expectProduct_whenProductExistsInDb() {
        long id = 1L;
        Product expected = Product.builder()
                .id(id)
                .name("Test Product")
                .price(BigDecimal.valueOf(10.00))
                .build();

        when(productRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Product actual = productService.getProductByIdEntity(id);

        assertThat(actual)
                .isNotNull()
                .satisfies(product -> {
                    assertThat(product.getId()).isEqualTo(expected.getId());
                    assertThat(product.getName()).isEqualTo(expected.getName());
                    assertThat(product.getPrice()).isEqualByComparingTo(expected.getPrice());
                });
    }

    @Test
    void getProductByIdEntity_expectProductNotFoundException_whenProductDoesNotExistInDb() {
        long nonExistentId = 999L;

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductByIdEntity(nonExistentId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found with id: " + nonExistentId);
    }

    @Test
    void createProduct_expectProduct_whenProductIsCreated() {
        CreateProductDto createProductDto = new CreateProductDto("Test Product", BigDecimal.valueOf(10.00));
        Product expectedProduct = Product.builder()
                .id(1L)
                .name(createProductDto.name())
                .price(createProductDto.price())
                .build();

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(expectedProduct);

        ProductResponseDto createdProduct = productService.createProduct(createProductDto);

        assertThat(createdProduct)
                .isNotNull()
                .satisfies(product -> {
                    assertThat(product.id()).isEqualTo(expectedProduct.getId());
                    assertThat(product.name()).isEqualTo(expectedProduct.getName());
                    assertThat(product.price()).isEqualByComparingTo(expectedProduct.getPrice());
                });
    }

    @Test
    void createProduct_shouldThrowException_whenNameIsBlank() {
        CreateProductDto dto = new CreateProductDto(" ", BigDecimal.valueOf(5.00));

        when(productRepository.save(Mockito.any(Product.class)))
                .thenThrow(new ConstraintViolationException("Product name is mandatory", null));

        assertThatThrownBy(() -> productService.createProduct(dto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Product name is mandatory");
    }

    @Test
    void createProduct_shouldThrowException_whenPriceIsNegative() {
        CreateProductDto dto = new CreateProductDto("Coffee", BigDecimal.valueOf(-2.50));

        when(productRepository.save(Mockito.any(Product.class)))
                .thenThrow(new ConstraintViolationException("Price must be positive", null));

        assertThatThrownBy(() -> productService.createProduct(dto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Price must be positive");
    }
}
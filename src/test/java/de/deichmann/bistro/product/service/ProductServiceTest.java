package de.deichmann.bistro.product.service;

import de.deichmann.bistro.product.dto.CreateProductDto;
import de.deichmann.bistro.product.dto.ProductResponseDto;
import de.deichmann.bistro.product.entity.Product;
import de.deichmann.bistro.product.exception.ProductNotFoundException;
import de.deichmann.bistro.product.repository.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

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
        Product product = Product.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(10.00))
                .build();
        productRepository.save(product);

        List<ProductResponseDto> products = productService.getAllProducts();

        assertThat(products)
                .isNotEmpty()
                .extracting(ProductResponseDto::name)
                .containsExactly("Test Product");
    }

    @Test
    void getAllProducts_shouldNotFail_withLargeDataset() {
        for (int i = 1; i <= 1000; i++) {
            productRepository.save(Product.builder()
                    .name("p" + i)
                    .price(BigDecimal.valueOf(i))
                    .build()
            );
        }
        List<ProductResponseDto> products = productService.getAllProducts();
        assertThat(products)
                .hasSize(1000);
    }

    @Test
    void getAllProducts_shouldMapFieldsCorrectly() {
        Product saved = productRepository.save(Product.builder()
                .name("Latte Macchiato")
                .price(BigDecimal.valueOf(3.50))
                .build());

        List<ProductResponseDto> products = productService.getAllProducts();

        assertThat(products)
                .hasSize(1)
                .first()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(saved.getId());
                    assertThat(dto.name()).isEqualTo(saved.getName());
                    assertThat(dto.price()).isEqualByComparingTo(saved.getPrice());
                });
    }

    @Test
    void getProductById_expectProduct_whenProductExistsInDb() {
        Product product = Product.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(10.00))
                .build();
        Product savedProduct = productRepository.save(product);

        ProductResponseDto productResponseDto = productService.getProductById(savedProduct.getId());

        assertThat(productResponseDto)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(savedProduct.getId());
                    assertThat(dto.name()).isEqualTo(savedProduct.getName());
                    assertThat(dto.price()).isEqualByComparingTo(savedProduct.getPrice());
                });
    }

    @Test
    void getProductById_expectProductNotFoundException_whenProductDoesNotExistInDb() {
        long nonExistentId = 999L;

        try {
            productService.getProductById(nonExistentId);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessageContaining("Product not found with id: " + nonExistentId);
        }
    }

    @Test
    void createProduct_expectProduct_whenProductIsCreated() {
        CreateProductDto createProductDto = new CreateProductDto("Test Product", BigDecimal.valueOf(10.00));

        ProductResponseDto createdProduct = productService.createProduct(createProductDto);

        assertThat(createdProduct)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.name()).isEqualTo(createProductDto.name());
                    assertThat(dto.price()).isEqualByComparingTo(createProductDto.price());
                    assertThat(dto.id()).isNotNull();
                });
    }

    @Test
    void createProduct_shouldThrowException_whenNameIsBlank() {
        CreateProductDto dto = new CreateProductDto(" ", BigDecimal.valueOf(5.00));

        assertThatThrownBy(() -> productService.createProduct(dto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Product name is mandatory");
    }

    @Test
    void createProduct_shouldThrowException_whenPriceIsNegative() {
        CreateProductDto dto = new CreateProductDto("Coffee", BigDecimal.valueOf(-2.50));

        assertThatThrownBy(() -> productService.createProduct(dto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Price must be positive");
    }

}
package de.deichmann.bistro.product.service;

import de.deichmann.bistro.product.dto.ProductResponseDto;
import de.deichmann.bistro.product.entity.Product;
import de.deichmann.bistro.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

}
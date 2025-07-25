package de.deichmann.bistro.product.config;

import de.deichmann.bistro.product.service.ProductService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ProductServiceTestConfig {

    @Bean
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }

}

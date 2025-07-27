package com.github.marcelldechant.bistro.product.integration;

import com.github.marcelldechant.bistro.product.dto.CreateProductDto;
import com.github.marcelldechant.bistro.product.exception.FileReadException;
import com.github.marcelldechant.bistro.product.service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;

import java.io.File;
import java.util.Objects;
import java.util.Set;

@Configuration
@EnableIntegration
@Slf4j
@RequiredArgsConstructor
public class CsvFileIntegrationConfig {
    @Value("${csv.input-directory}")
    private String inputDirectory;

    private final Validator validator;

    @Bean
    public IntegrationFlow fileReadingFlow(CsvToProductTransformer transformer, ProductService productService) {

        return IntegrationFlow
                .from(Files.inboundAdapter(new File(inputDirectory))
                                .patternFilter("*.csv")
                                .preventDuplicates(true),
                        e -> e.poller(Pollers.fixedDelay(5000)))
                .transform(File.class, file -> {
                    log.info("Importing CSV file: {}", file.getName());
                    try {
                        return java.nio.file.Files.readString(file.toPath());
                    } catch (Exception e) {
                        log.error("Failed to read file: {}", file.getName(), e);
                        throw new FileReadException("Failed to read file", e);
                    }
                })
                .split(String.class, s -> s.split("\n"))
                .filter(line -> {
                    String trimmed = ((String) line).trim();
                    return !trimmed.isEmpty() && !trimmed.startsWith("name");
                })
                .transform(transformer)
                .filter(Objects::nonNull)
                .handle((GenericHandler<CreateProductDto>) (product, headers) -> {
                    if (!isValid(product)) return null;
                    productService.createProduct(product);
                    return null;
                })
                .channel("nullChannel")
                .get();
    }

    private boolean isValid(CreateProductDto product) {
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            log.error("Invalid product {}: {}", product, violations);
            return false;
        }
        return true;
    }
}

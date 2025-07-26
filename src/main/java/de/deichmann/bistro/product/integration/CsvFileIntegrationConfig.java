package de.deichmann.bistro.product.integration;

import de.deichmann.bistro.product.dto.CreateProductDto;
import de.deichmann.bistro.product.service.ProductService;
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

/**
 * Configuration class for integrating CSV file reading into the Bistro application.
 * This class sets up an integration flow that reads CSV files from a specified directory,
 * transforms the content into Product objects, validates them, and saves them using the ProductService.
 * It uses Spring Integration to handle file reading and processing.
 *
 * @author Marcell Dechant
 */
@Configuration
@EnableIntegration
@Slf4j
@RequiredArgsConstructor
public class CsvFileIntegrationConfig {

    @Value("${csv.input-directory}")
    private String inputDirectory;

    private final Validator validator;

    /**
     * Bean definition for the CsvToProductTransformer.
     * This transformer converts CSV lines into CreateProductDto objects.
     *
     * @return a CsvToProductTransformer instance
     */
    @Bean
    public IntegrationFlow fileReadingFlow(CsvToProductTransformer transformer, ProductService productService) {
        return IntegrationFlow
                .from(Files.inboundAdapter(new File(inputDirectory))
                                .patternFilter("*.csv")
                                .preventDuplicates(true),
                        e -> e.poller(Pollers.fixedDelay(5000))) // Poll every 5 seconds
                .transform(File.class, file -> {
                    log.info("Importing CSV file: {}", file.getName());
                    try {
                        return java.nio.file.Files.readString(file.toPath());
                    } catch (Exception e) {
                        log.error("Failed to read file: {}", file.getName(), e);
                        throw new RuntimeException("Failed to read file", e);
                    }
                }) // Read file content as String
                .split(String.class, s -> s.split("\n")) // Split file content into lines
                .filter(line -> {
                    String trimmed = ((String) line).trim();
                    return !trimmed.isEmpty() && !trimmed.startsWith("name");
                }) // Filter out empty lines and header
                .transform(transformer) // Transform String[] to Product
                .filter(Objects::nonNull) // Filter out null products (invalid lines)
                .handle((GenericHandler<CreateProductDto>) (product, headers) -> {
                    if (!isValid(product)) return null;
                    productService.createProduct(product);
                    return null;
                }) // Save Product to a repository
                .channel("nullChannel") // Use a null channel to discard the output
                .get();
    }

    /**
     * Validates the CreateProductDto object.
     * If the object is invalid, it logs the violations and returns false.
     *
     * @param product createProductDto to validate
     * @return true if valid, false otherwise
     */
    private boolean isValid(CreateProductDto product) {
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            log.error("Invalid product {}: {}", product, violations);
            return false;
        }
        return true;
    }

}

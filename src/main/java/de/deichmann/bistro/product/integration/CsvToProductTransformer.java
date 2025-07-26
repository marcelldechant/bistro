package de.deichmann.bistro.product.integration;

import de.deichmann.bistro.product.dto.CreateProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Transformer that converts a CSV line (String array) into a CreateProductDto.
 * It trims the product name and converts the price from String to BigDecimal.
 * If the product name is blank or the price format is invalid, it logs an error.
 *
 * @author Marcell Dechant
 */
@Component
@Slf4j
public class CsvToProductTransformer implements GenericTransformer<String[], CreateProductDto> {

    @Override
    public CreateProductDto transform(String[] line) {
        String rawName = line[0].trim();
        if (rawName.isBlank()) {
            log.error("Blank product name: {}", (Object) line);
            return null;
        }
        String rawPrice = line[1].trim().replace(",", ".");

        try {
            BigDecimal price = new BigDecimal(rawPrice);
            return new CreateProductDto(rawName, price);
        } catch (NumberFormatException e) {
            log.error("Invalid price format for line: {}, rawPrice: {}", line, rawPrice);
            return null;
        }
    }

}

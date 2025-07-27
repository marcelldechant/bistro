package com.github.marcelldechant.bistro.product.dto;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        BigDecimal price
) {
}

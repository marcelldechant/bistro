package com.github.marcelldechant.bistro.exception.dto;

import java.time.Instant;

public record CustomApiErrorResponseDto(
        String message,
        String path,
        Instant timestamp,
        int statusCode
) {
}

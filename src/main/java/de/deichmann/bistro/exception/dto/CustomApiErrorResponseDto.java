package de.deichmann.bistro.exception.dto;

import java.time.Instant;

/**
 * Data Transfer Object for custom API error responses.
 * This class is used to encapsulate error details returned by the API when an exception occurs.
 * It includes the error message, request path, timestamp of the error, and HTTP status code.
 *
 * @param message    the error message describing the issue
 * @param path       the request URI that caused the error
 * @param timestamp  the time when the error occurred
 * @param statusCode the HTTP status code associated with the error
 */
public record CustomApiErrorResponseDto(
        String message,
        String path,
        Instant timestamp,
        int statusCode
) {
}

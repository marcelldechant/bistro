package com.github.marcelldechant.bistro.exception.handler;

import com.github.marcelldechant.bistro.exception.dto.CustomApiErrorResponseDto;
import com.github.marcelldechant.bistro.order.exception.OrderNotFoundException;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global exception handler for the Bistro application.
 * This class handles exceptions thrown by controllers and returns a standardized error response.
 * It uses @RestControllerAdvice to apply to all controllers in the application.
 *
 * @author Marcell Dechant
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all exceptions that are not specifically handled by other exception handlers.
     * Returns a CustomApiErrorResponseDto with the error message, request URI, timestamp, and HTTP status code.
     *
     * @param e       the exception that was thrown
     * @param request the HTTP request that caused the exception
     * @return a CustomApiErrorResponseDto containing error details
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomApiErrorResponseDto handleException(Exception e, HttpServletRequest request) {

        return new CustomApiErrorResponseDto(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

    }

    /**
     * Handles ProductNotFoundException specifically.
     * Returns a CustomApiErrorResponseDto with the error message, request URI, timestamp, and HTTP status code 404 (Not Found).
     *
     * @param e       the ProductNotFoundException that was thrown
     * @param request the HTTP request that caused the exception
     * @return a CustomApiErrorResponseDto containing error details
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomApiErrorResponseDto handleProductNotFoundException(ProductNotFoundException e, HttpServletRequest request) {

        return new CustomApiErrorResponseDto(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                HttpStatus.NOT_FOUND.value()
        );

    }

    /**
     * Handles OrderNotFoundException specifically.
     * Returns a CustomApiErrorResponseDto with the error message, request URI, timestamp, and HTTP status code 404 (Not Found).
     *
     * @param e       the OrderNotFoundException that was thrown
     * @param request the HTTP request that caused the exception
     * @return a CustomApiErrorResponseDto containing error details
     */
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomApiErrorResponseDto handleOrderNotFoundException(OrderNotFoundException e, HttpServletRequest request) {
        return new CustomApiErrorResponseDto(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                HttpStatus.NOT_FOUND.value()
        );
    }

}

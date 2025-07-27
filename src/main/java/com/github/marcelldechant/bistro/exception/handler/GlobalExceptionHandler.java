package com.github.marcelldechant.bistro.exception.handler;

import com.github.marcelldechant.bistro.exception.dto.CustomApiErrorResponseDto;
import com.github.marcelldechant.bistro.order.exception.DuplicateException;
import com.github.marcelldechant.bistro.order.exception.NoItemsException;
import com.github.marcelldechant.bistro.order.exception.OrderNotFoundException;
import com.github.marcelldechant.bistro.order.exception.QuantityException;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
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

    @Hidden
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

    @Hidden
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

    @Hidden
    @ExceptionHandler(NoItemsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomApiErrorResponseDto handleNoItemsException(NoItemsException e, HttpServletRequest request) {
        return new CustomApiErrorResponseDto(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                HttpStatus.BAD_REQUEST.value()
        );
    }

    @Hidden
    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomApiErrorResponseDto handleDuplicateException(DuplicateException e, HttpServletRequest request) {
        return new CustomApiErrorResponseDto(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                HttpStatus.CONFLICT.value()
        );
    }

    @Hidden
    @ExceptionHandler(QuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomApiErrorResponseDto handleQuantityException(QuantityException e, HttpServletRequest request) {
        return new CustomApiErrorResponseDto(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                HttpStatus.BAD_REQUEST.value()
        );
    }
}

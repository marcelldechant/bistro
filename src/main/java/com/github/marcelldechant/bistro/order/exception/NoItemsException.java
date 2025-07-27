package com.github.marcelldechant.bistro.order.exception;

public class NoItemsException extends RuntimeException {
    public NoItemsException(String message) {
        super(message);
    }
}

package com.github.marcelldechant.bistro.order.exception;

/**
 * Exception thrown when there is an issue with the quantity of an order.
 * This exception is used to indicate that the requested quantity is invalid or not allowed.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 */
public class QuantityException extends RuntimeException {
    /**
     * Constructs a new QuantityException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the getMessage() method
     */
    public QuantityException(String message) {
        super(message);
    }
}

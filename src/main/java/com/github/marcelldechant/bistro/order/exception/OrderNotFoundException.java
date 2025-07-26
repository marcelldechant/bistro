package com.github.marcelldechant.bistro.order.exception;

/**
 * Exception thrown when an order is not found.
 * This exception is used to indicate that a requested order does not exist in the system.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 *
 * @author Marcell Dechant
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Constructs a new OrderNotFoundException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the getMessage() method
     */
    public OrderNotFoundException(String message) {
        super(message);
    }

}

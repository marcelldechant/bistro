package com.github.marcelldechant.bistro.order.exception;

/**
 * Exception thrown when an order has no items.
 * This exception is used to indicate that an order cannot be processed because it does not contain any items.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 *
 * @author Marcell Dechant
 */
public class NoItemsException extends RuntimeException {
    /**
     * Constructs a new NoItemsException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the getMessage() method
     */
    public NoItemsException(String message) {
        super(message);
    }
}

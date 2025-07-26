package com.github.marcelldechant.bistro.order.exception;

/**
 * Exception thrown when a duplicate entry is detected in the system.
 * This exception is used to indicate that an attempt was made to create or update an entity
 * that already exists, violating uniqueness constraints.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 *
 * @author Marcell Dechant
 */
public class DuplicateException extends RuntimeException {
    /**
     * Constructs a new DuplicateException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the getMessage() method
     */
    public DuplicateException(String message) {
        super(message);
    }
}

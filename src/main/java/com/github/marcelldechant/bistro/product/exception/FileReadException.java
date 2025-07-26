package com.github.marcelldechant.bistro.product.exception;

/**
 * Exception thrown when there is an error reading a file.
 * This exception is used to indicate that a file could not be read, possibly due to
 * issues such as file not found, permission denied, or other I/O errors.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 *
 * @author Marcell Dechant
 */
public class FileReadException extends RuntimeException {

    /**
     * Constructs a new FileReadException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the getCause() method)
     */
    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }

}

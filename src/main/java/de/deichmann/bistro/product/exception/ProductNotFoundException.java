package de.deichmann.bistro.product.exception;

/**
 * Exception thrown when a product is not found.
 * This exception is used to indicate that a requested product does not exist in the system.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 *
 * @author Marcell Dechant
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a new ProductNotFoundException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the getMessage() method
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

}

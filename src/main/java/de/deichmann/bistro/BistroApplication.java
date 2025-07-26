package de.deichmann.bistro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Bistro application.
 * This class serves as the entry point for the Spring Boot application.
 * It is annotated with @SpringBootApplication, which enables autoconfiguration,
 * component scanning, and configuration properties support.
 *
 * @author Marcell Dechant
 */
@SpringBootApplication
public class BistroApplication {

    /**
     * The main method that starts the Bistro application.
     * It uses SpringApplication. Run to launch the application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BistroApplication.class, args);
    }

}

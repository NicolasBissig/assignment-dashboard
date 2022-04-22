package edu.hm.hafner.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Entry point for this Spring Boot Application.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"CheckStyle", "NonFinalUtilityClass", "HideUtilityClassConstructor"})
@SpringBootApplication
public class AnalysisDashboard {
    /**
     * Starts the application.
     *
     * @param args
     *         optional commandline arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(AnalysisDashboard.class, args);
    }
}

package com.example.UserAuthentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for web MVC settings.
 * This class is used to configure CORS (Cross-Origin Resource Sharing) settings to control how resources
 * in the web application can be requested from another domain.
 * This setup is crucial for a multi-origin environment where the backend and frontend run on different servers or ports.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mapping for the application.
     * This method sets up CORS rules that apply to API paths to ensure the frontend can interact with the backend securely.
     *
     * @param registry the CORS registry to which the CORS rules are added.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Apply CORS settings to all API paths
        registry.addMapping("/api/**")  // Adjust this to target specific paths or use "/*" for global CORS
                .allowedOrigins("http://localhost:3000")  // Specify the allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Specify the allowed methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true)  // Whether user credentials are supported
                .maxAge(3600);  // How long the preflight response can be cached (in seconds)
    }
}

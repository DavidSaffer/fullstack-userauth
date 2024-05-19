package com.example.UserAuthentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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

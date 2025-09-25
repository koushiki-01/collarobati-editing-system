package com.collab.version.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //   HTTP API endpoints
                        .allowedOrigins("http://localhost:8080", "http://localhost:5173") // add frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/ws/**") //  WebSocket endpoint
                        .allowedOrigins("http://localhost:8080", "http://localhost:5173")
                        .allowCredentials(true);
            }
        };
    }
}

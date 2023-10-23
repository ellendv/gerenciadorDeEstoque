package com.control.estoquemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Permite solicitações de qualquer origem
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite métodos HTTP específicos
                .allowedHeaders("*"); // Permite todos os cabeçalhos
    }
}

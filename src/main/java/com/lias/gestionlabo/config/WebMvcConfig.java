package com.lias.gestionlabo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // Photos de profil + PDFs de publications
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Alias /images/** → même dossier uploads/ (utilisé par getTransientPhotoImagePath)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/");

        // PDFs des publications
        registry.addResourceHandler("/uploads/publications/**")
                .addResourceLocations("file:uploads/publications/");
    }
}
package com.microservices.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuración de Cloudinary para el manejo de imágenes
 * Configura la instancia de Cloudinary con las credenciales de la aplicación
 */
@Configuration
@Slf4j
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.secure:true}")
    private boolean secure;

    @Value("${cloudinary.upload-preset:}")
    private String uploadPreset;

    /**
     * Bean de configuración para Cloudinary
     * @return instancia configurada de Cloudinary
     */
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", secure
        );

        log.info("Configurando Cloudinary con cloud_name: {}", cloudName);
        
        return new Cloudinary(config);
    }
}

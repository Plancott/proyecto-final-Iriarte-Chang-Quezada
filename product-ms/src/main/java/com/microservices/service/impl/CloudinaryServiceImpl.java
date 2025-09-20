package com.microservices.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.microservices.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementación del servicio de Cloudinary para manejo de imágenes
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    
    @Value("${cloudinary.upload-preset:}")
    private String uploadPreset;
    
    // Formatos de imagen permitidos
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    
    // Tamaño máximo del archivo en bytes (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        validateFile(file);
        
        try {
            log.info("Subiendo imagen: {} a la carpeta: {}", file.getOriginalFilename(), folder);
            
            // Para upload presets unsigned, usar configuración mínima
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "upload_preset", uploadPreset,
                "folder", folder,
                "timeout", 60000 // 60 segundos timeout
            );
            
            log.info("Usando upload preset unsigned: {} con carpeta: {}", uploadPreset, folder);
            
            log.info("Parámetros de subida: {}", uploadParams);
            
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(), 
                uploadParams
            );
            
            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Imagen subida exitosamente. URL: {}", imageUrl);
            
            return imageUrl;
            
        } catch (IOException e) {
            log.error("Error de IO al subir imagen: {}", e.getMessage(), e);
            throw new RuntimeException("Error de conectividad al subir la imagen: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado al subir imagen: {}", e.getMessage(), e);
            throw new RuntimeException("Error inesperado al subir la imagen: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadProductImage(MultipartFile file) {
        return uploadImage(file, "products");
    }

    @Override
    public boolean deleteImage(String publicId) {
        try {
            log.info("Eliminando imagen con public_id: {}", publicId);
            
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            
            boolean success = "ok".equals(resultStatus);
            log.info("Resultado de eliminación: {}", success ? "Éxito" : "Falló");
            
            return success;
            
        } catch (Exception e) {
            log.error("Error al eliminar imagen: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String extractPublicId(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        
        try {
            // Cloudinary URLs tienen el formato:
            // https://res.cloudinary.com/cloud-name/image/upload/v1234567890/folder/filename.jpg
            
            String[] urlParts = imageUrl.split("/upload/");
            if (urlParts.length < 2) {
                return null;
            }
            
            String pathPart = urlParts[1];
            // Remover el timestamp si existe (v1234567890/)
            if (pathPart.startsWith("v") && pathPart.contains("/")) {
                pathPart = pathPart.substring(pathPart.indexOf("/") + 1);
            }
            
            // Remover la extensión del archivo
            int lastDotIndex = pathPart.lastIndexOf(".");
            if (lastDotIndex != -1) {
                pathPart = pathPart.substring(0, lastDotIndex);
            }
            
            log.debug("Public ID extraído: {}", pathPart);
            return pathPart;
            
        } catch (Exception e) {
            log.error("Error al extraer public ID de URL: {}", imageUrl, e);
            return null;
        }
    }

    /**
     * Valida que el archivo sea una imagen válida
     * @param file archivo a validar
     * @throws RuntimeException si el archivo no es válido
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("El archivo no puede estar vacío");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("El archivo es demasiado grande. Máximo permitido: 5MB");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new RuntimeException("Tipo de archivo no permitido. Solo se permiten: JPEG, PNG, GIF, WebP");
        }
        
        log.debug("Archivo validado correctamente: {} ({} bytes, {})", 
                 file.getOriginalFilename(), file.getSize(), contentType);
    }

    /**
     * Genera un public ID único para la imagen
     * @param originalFilename nombre original del archivo
     * @return public ID generado
     */
    private String generatePublicId(String originalFilename) {
        if (originalFilename == null) {
            return "image_" + System.currentTimeMillis();
        }
        
        // Remover extensión y caracteres especiales
        String nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String cleanName = nameWithoutExt.replaceAll("[^a-zA-Z0-9_-]", "_");
        
        // Agregar timestamp para evitar duplicados
        return cleanName + "_" + System.currentTimeMillis();
    }
}

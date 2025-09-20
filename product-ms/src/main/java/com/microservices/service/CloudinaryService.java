package com.microservices.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio para manejo de imágenes con Cloudinary
 */
public interface CloudinaryService {
    
    /**
     * Sube una imagen a Cloudinary y retorna la URL pública
     * @param file archivo de imagen a subir
     * @param folder carpeta donde se almacenará la imagen
     * @return URL pública de la imagen subida
     * @throws RuntimeException si ocurre un error durante la subida
     */
    String uploadImage(MultipartFile file, String folder);
    
    /**
     * Sube una imagen a Cloudinary en la carpeta de productos
     * @param file archivo de imagen a subir
     * @return URL pública de la imagen subida
     * @throws RuntimeException si ocurre un error durante la subida
     */
    String uploadProductImage(MultipartFile file);
    
    /**
     * Elimina una imagen de Cloudinary
     * @param publicId ID público de la imagen en Cloudinary
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean deleteImage(String publicId);
    
    /**
     * Extrae el public ID de una URL de Cloudinary
     * @param imageUrl URL de la imagen en Cloudinary
     * @return public ID de la imagen o null si no se puede extraer
     */
    String extractPublicId(String imageUrl);
}

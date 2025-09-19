package com.microservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "product_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"productId"})
@ToString
public class Product {
    // Configuración de la entidad JPA con tabla personalizada, restricciones únicas y auditoría automática

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId; // Identificador único auto-generado

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description; // dataProduct renombrado a description para mayor claridad

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "La categoría es obligatoria")
    private Category category; // Relación con la entidad Category

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio unitario debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal unitPrice;

    @Column(name = "image_url", length = 500)
    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    @Pattern(regexp = "^(https?://.*|)$", message = "La URL de la imagen debe ser válida o estar vacía")
    private String imageUrl; // URL del link firmado de la imagen

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "La marca es obligatoria")
    private Brand brand; // Relación con la entidad Brand

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "El estado del producto es obligatorio")
    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE; // Estado por defecto activo


    /**
     * Constructor personalizado para crear un producto con datos básicos
     */
    public Product(String name, String description, Category category, BigDecimal unitPrice, 
                   String imageUrl, Brand brand, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.unitPrice = unitPrice;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.status = status != null ? status : ProductStatus.ACTIVE;
    }

    /**
     * Verifica si el producto está activo
     * @return true si el producto está activo, false en caso contrario
     */
    public boolean isActive() {
        return this.status != null && this.status.isActive();
    }

    /**
     * Activa el producto
     */
    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }

    /**
     * Desactiva el producto
     */
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    /**
     * Asigna una categoría al producto
     * @param category la categoría a asignar
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Asigna una marca al producto
     * @param brand la marca a asignar
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    /**
     * Obtiene el nombre de la categoría del producto
     * @return el nombre de la categoría o null si no tiene categoría
     */
    public String getCategoryName() {
        return this.category != null ? this.category.getNombreCategoria() : null;
    }

    /**
     * Obtiene el nombre de la marca del producto
     * @return el nombre de la marca o null si no tiene marca
     */
    public String getBrandName() {
        return this.brand != null ? this.brand.getNombreMarca() : null;
    }
}

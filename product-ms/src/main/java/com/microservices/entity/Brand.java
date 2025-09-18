package com.microservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "nombre_marca")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"marcaId"})
@ToString(exclude = {"products"})
public class Brand {
    // Configuración de la entidad JPA con tabla personalizada y restricciones únicas

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marcaid")
    private Long marcaId; // Identificador único auto-generado

    @Column(name = "nombre_marca", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre de la marca debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\-&.]+$", message = "El nombre de la marca solo puede contener letras, espacios, guiones, & y puntos")
    private String nombreMarca;

    // Relación uno a muchos con productos
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    /**
     * Constructor personalizado para crear una marca con datos básicos
     */
    public Brand(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    /**
     * Agrega un producto a esta marca
     * @param product el producto a agregar
     */
    public void addProduct(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
            product.setBrand(this);
        }
    }

    /**
     * Remueve un producto de esta marca
     * @param product el producto a remover
     */
    public void removeProduct(Product product) {
        if (product != null && products.contains(product)) {
            products.remove(product);
            product.setBrand(null);
        }
    }
}

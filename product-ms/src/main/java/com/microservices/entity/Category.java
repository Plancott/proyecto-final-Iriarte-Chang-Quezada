package com.microservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "nombre_categoria")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"idCategoria"})
@ToString(exclude = {"products"})
public class Category {
    // Configuración de la entidad JPA con tabla personalizada y restricciones únicas

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategoria")
    private Long idCategoria; // Identificador único auto-generado

    @Column(name = "nombre_categoria", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre de la categoría debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\-&]+$", message = "El nombre de la categoría solo puede contener letras, espacios, guiones y &")
    private String nombreCategoria;

    // Relación uno a muchos con productos
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    /**
     * Constructor personalizado para crear una categoría con datos básicos
     */
    public Category(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    /**
     * Agrega un producto a esta categoría
     * @param product el producto a agregar
     */
    public void addProduct(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
            product.setCategory(this);
        }
    }

    /**
     * Remueve un producto de esta categoría
     * @param product el producto a remover
     */
    public void removeProduct(Product product) {
        if (product != null && products.contains(product)) {
            products.remove(product);
            product.setCategory(null);
        }
    }
}

package org.example.stockms.model.stock;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.example.stockms.model.store.Store;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime date;

    private String state ="entrada";

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonBackReference
    private Store store; // a qué almacén pertenece

    private Long productId;
}

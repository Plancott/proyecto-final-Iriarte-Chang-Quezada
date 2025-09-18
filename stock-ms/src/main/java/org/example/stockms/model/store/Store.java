package org.example.stockms.model.store;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.example.stockms.model.stock.Stock;

import java.util.List;

@ToString
@Data
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    private Integer capacity = 0;
    private Integer capacityTotal;

    @OneToMany(mappedBy = "store")
    @JsonManagedReference
    private List<Stock> stocks;
}

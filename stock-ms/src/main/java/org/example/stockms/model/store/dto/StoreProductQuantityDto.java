package org.example.stockms.model.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreProductQuantityDto {
    private Long productId;
    private Long cantidadTotal;
}

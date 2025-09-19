package org.example.stockms.model.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreCantidadProductoAlmacenDto {
    private Long storeId;
    private List<StoreProductQuantityDto> productos;

    // Constructor vacío
    public StoreCantidadProductoAlmacenDto() {}

    // Constructor explícito
    public StoreCantidadProductoAlmacenDto(Long storeId, List<StoreProductQuantityDto> productos) {
        this.storeId = storeId;
        this.productos = productos;
    }
}

package org.example.stockms.model.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreCantidadProductoAlmacenDto {
    private Long storeId;
    private List<StoreProductQuantityDto> productos;
}

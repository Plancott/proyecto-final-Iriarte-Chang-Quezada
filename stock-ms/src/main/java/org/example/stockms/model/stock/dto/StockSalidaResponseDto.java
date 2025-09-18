package org.example.stockms.model.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StockSalidaResponseDto {
    private Long storeId;        // Id del almacén
    //private Long stockId;        // Id del stock
    private Integer cantidadRetirada; // Cantidad que se retiró
    private Integer cantidadRestante; // Cantidad que quedó en stock
    private Long productId;
}

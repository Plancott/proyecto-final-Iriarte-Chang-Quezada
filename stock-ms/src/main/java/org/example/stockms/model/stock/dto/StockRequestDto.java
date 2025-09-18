package org.example.stockms.model.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class StockRequestDto {

    @NotNull(message = "El ID de producto es obligatorio")
    private Long productId;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotNull(message = "El ID del almacén es obligatorio")
    private Long storeId;
}

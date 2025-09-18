package org.example.stockms.model.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequestDto {
    @NotBlank(message = "El nombre del almacén no puede estar vacío")
    private String name;

    @Min(value = 1, message = "La capacidad total debe ser al menos 1")
    private Integer capacityTotal;
}

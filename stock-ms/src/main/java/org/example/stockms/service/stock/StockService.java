package org.example.stockms.service.stock;

import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.*;

import java.util.List;

public interface StockService {
    // Crear (puede repartir en varios almacenes)
    List<Stock> save(List<StockRequestDto> stockRequestDtos);
    // Buscar por id
    Stock findById(Long id);

    // Restar stock de un producto
    List<StockSalidaResponseDto> restarStock(List<StockSalidaRequestDto> solicitudes);
    // Obtener todos
    List<Stock> findAll();

    //Eliminar un stock
    void deleteById(Long id);

    //Buscar entradas y salidad de un producto en especificoy saber cuanto hay en total
    StockFindProductDto findByProductId(Long productId);
}

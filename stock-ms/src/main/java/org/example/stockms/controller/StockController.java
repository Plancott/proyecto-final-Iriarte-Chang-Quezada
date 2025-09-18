package org.example.stockms.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.stockms.mapper.StockMapper;
import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.*;
import org.example.stockms.service.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockMapper stockMapper;
    private final StockService stockService;

//    /*
//    - Crear stock inicial recibe un StockRequestDto
//    {
//        private Integer productId; -> El id del producto que se va a registrar
//        private Integer quantity; -> La cantidad del producto
//        private Integer storeId; -> El id del almacen en donde se va a guardar
//    }
//
//    - Devuelve una lista de los stocks registrados, digo stocks ya que en caso la cantidad
//    que queremos registrar excede a la capacidad maxima o el almacen este lleno, entonces crea
//    otro almacen y guarda el restante ahi
//    {
//        private Integer id; -> Id del registro del stock
//        private Integer productId; -> Id del producto registrado
//        private Integer quantity; -> Cantidad registrada
//        private Integer storeId; -> Almacen en donde se guardaron los productos
//        private String state; -> El estado, en este caso entrada
//        private LocalDateTime date; -> La fecha de creacion del registro
//    }
//    */
//    @PostMapping("/entrada")
//    public ResponseEntity<List<StockResponseDto>> save(@Valid @RequestBody StockRequestDto stockRequestDto) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(stockMapper.toListStockResponseDto(stockService.save(stockRequestDto)));
//    }

    @PostMapping("/entrada")
    public ResponseEntity<List<StockResponseDto>> saveAllStocks(@RequestBody List<StockRequestDto> stockRequestDtos) {
        List<Stock> savedStocks = stockService.save(stockRequestDtos);
        List<StockResponseDto> response = stockMapper.toListStockResponseDto(savedStocks);
        return ResponseEntity.ok(response);
    }

    /* Devuelve una lista de todos los stocks registrados
    {
        private Integer id;
        private Integer productId;
        private Integer quantity;
        private Integer storeId;
        private String state;
        private LocalDateTime date;
    }
    */
    @GetMapping
    public ResponseEntity<List<StockResponseDto>> findAll() {
        return ResponseEntity.ok(stockMapper.toListStockResponseDto(stockService.findAll()));
    }

    /* Devuelve un stock segun el id que deseamos buscar
        {
            private Integer id;
            private Integer productId;
            private Integer quantity;
            private Integer storeId;
            private String state;
            private LocalDateTime date;
        }
    */
    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockMapper.toStockResponseDto(stockService.findById(id)));
    }

    @GetMapping("/totalProduct/{id}")
    public ResponseEntity<StockFindProductDto> findByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findByProductId(id));
    }

    /*
        - Pide los datos de salida de stock de un producto en especifico
        {
            private Integer productId;
            private Integer quantity;
        }
        - Devuelve una lista de los stock retirados ya que si en el almacen no hay tanta
        cantidad del producto que buscamos pues busca en otro almacen y retira de ahi
        {
            private Integer storeId;        -> Id del almacén del cual se hizo el retiro
            private Integer stockId;        -> Id del stock, ya que cada retiro o salida tiene su id
            private int cantidadRetirada; // Cantidad que se retiró
            private int cantidadRestante; // Cantidad que quedó en stock segun el almacen
            private int productId;        // Id del producto
        }
    */
    @PostMapping("/salida")
    public ResponseEntity<List<StockSalidaResponseDto>> restarStock(
            @Valid @RequestBody List<StockSalidaRequestDto> solicitudes) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(stockService.restarStock(solicitudes));
    }


    //----------
    //Eliminar un stock
    @PatchMapping("/{id}")
    public ResponseEntity<StockResponseDto> patchStock(
            @PathVariable Long id,
            @RequestBody StockRequestDto stockRequestDto) {
        return ResponseEntity.ok(stockMapper.toStockResponseDto(stockService.updateById(id, stockRequestDto)));
    }


}

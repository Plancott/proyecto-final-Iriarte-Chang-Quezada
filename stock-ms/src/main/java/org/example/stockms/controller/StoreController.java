package org.example.stockms.controller;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.example.stockms.mapper.StoreMapper;
import org.example.stockms.model.store.dto.StoreCantidadProductoAlmacenDto;
import org.example.stockms.model.store.dto.StoreRequestDto;
import org.example.stockms.model.store.dto.StoreResponseDto;
import org.example.stockms.service.store.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/store")
public class StoreController {
    private final StoreService storeService;
    private final StoreMapper storeMapper;



    /*
    Retorna una lista de {
        private Integer id; -> El id del almacen
        private String name; -> El nombre del almacen
        private Integer capacity; -> La capacidad ocupada
        private Integer capacityTotal; -> La capacidad total
        private List<Stock> stocks; -> Lista de stocks registrados en el almacen}
    */
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getStores(){
        return ResponseEntity.ok(storeMapper.toListResponseDto(storeService.findAllStores()));
    }

    //Retorna un almacen buscado por id
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long id){
        return ResponseEntity.ok(storeMapper.toStoreResponseDto(storeService.findStoreById(id)));
    }

    //Obtener el total de productos por almacen
    @GetMapping("/productosAlmacen")
    public ResponseEntity<List<StoreCantidadProductoAlmacenDto>> productosAlmacen() {
        return ResponseEntity.ok(storeService.findAllStoreProductosDto());
    }

    /* -Crea un nuevo almacen(Store)pide los datos
    {private String name; -> El nombre el almacen
        private Integer capacityTotal; -> La capacidad maxima}

        -Devuelve un {
        private Integer id; -> El id del almacen
        private String name; -> El nombre del almacen
        private Integer capacity; -> La capacidad ocupada
        private Integer capacityTotal; -> La capacidad total
        private List<Stock> stocks; -> Lista de stocks registrados en el almacen
        }
    */
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto storeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeMapper.toStoreResponseDto(storeService.saveStore(storeMapper.toStore(storeRequestDto))));
    }

    //Eliminamos un store por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id){
        storeService.deleteStoreById(id);
        return ResponseEntity.noContent().build();
    }

    //Actualizar un store
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto>  updateStore(@PathVariable Long id, @RequestBody StoreRequestDto storeRequestDto){
        return ResponseEntity.ok(storeMapper.toStoreResponseDto(storeService.updateStoreById(id, storeMapper.toStore(storeRequestDto))));
    }


}

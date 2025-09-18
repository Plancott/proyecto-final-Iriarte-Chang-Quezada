package org.example.stockms.service.store;

import org.example.stockms.model.store.Store;
import org.example.stockms.model.store.dto.StoreCantidadProductoAlmacenDto;

import java.util.List;

public interface StoreService {
    //Crear almacen
    Store saveStore(Store store);

    //Obtener todos los almacenes
    List<Store> findAllStores();

    //Obtener por id
    Store findStoreById(Long id);

    //Eliminar un almacen pero no los registros de stock
    void deleteStoreById(Long id);

    //Actualizar un store
    Store updateStoreById(Long id, Store store);

    //Cantidada de productos por almacen
    List<StoreCantidadProductoAlmacenDto> findAllStoreProductosDto();
}

package org.example.stockms.service.store;

import lombok.AllArgsConstructor;
import org.example.stockms.exception.StoreNotEmptyException;
import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.store.Store;
import org.example.stockms.model.store.dto.StoreCantidadProductoAlmacenDto;
import org.example.stockms.model.store.dto.StoreProductQuantityDto;
import org.example.stockms.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    //Crea un nuevo almacen y devuelve los datos del mismo
//    @Override
//    public Store saveStore(Store store) {
//        return storeRepository.save(store);
//    }
    @Override
    public List<Store> saveStore(Integer cantidadStores) {
        List<Store> stores = new ArrayList<>();
        long totalExistentes = storeRepository.count();

        for (int i = 1; i <= cantidadStores; i++) {
            Store store = new Store();
            store.setName("store" + (totalExistentes + i));     // Nombre: store1, store2, store3...
            store.setCapacity(0);           // Capacidad inicial en 0
            store.setCapacityTotal(100);    // Capacidad máxima definida
            stores.add(store);
        }

        // Guardamos todos en una sola operación
        return storeRepository.saveAll(stores);
    }


    //Devuelve todos los almacenes con sus datos
    @Override
    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    //Busca un almacen por id y devuelve sus datos y contenido
    @Override
    public Store findStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Store not found"));
    }

    //Eliminar un almacen
    @Override
    public void deleteStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store no encontrada"));

        if (!store.getStocks().isEmpty()) {
            throw new StoreNotEmptyException(store.getId(),store.getCapacity());
        }

        storeRepository.deleteById(id);
    }


    @Override
    public Store updateStoreById(Long id, Store store) {
        System.out.println(store);
        Store storeActual = findStoreById(id);
        storeActual.setName(store.getName());
        storeActual.setCapacity(storeActual.getCapacity());
        storeActual.setCapacityTotal(store.getCapacityTotal());

        return storeRepository.save(storeActual);
    }

    @Override
    public List<StoreCantidadProductoAlmacenDto> findAllStoreProductosDto() {
        return storeRepository.findAll()
                .stream()
                .map(store -> {
                    Map<Long, Integer> productosAgrupados = store.getStocks().stream()
                            .collect(Collectors.groupingBy(
                                    Stock::getProductId,
                                    Collectors.summingInt(stock ->
                                            "entrada".equalsIgnoreCase(stock.getState())
                                                    ? stock.getQuantity()
                                                    : -stock.getQuantity()
                                    )
                            ));

                    List<StoreProductQuantityDto> productos = productosAgrupados.entrySet()
                            .stream()
                            .map(entry -> new StoreProductQuantityDto(
                                    entry.getKey(),
                                    entry.getValue().longValue()
                            ))
                            .toList();

                    return new StoreCantidadProductoAlmacenDto(store.getId().longValue(), productos);
                })
                .toList();
    }
}

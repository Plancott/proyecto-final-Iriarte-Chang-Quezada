package org.example.stockms.service.store;

import lombok.AllArgsConstructor;
import org.example.stockms.exception.StoreNotEmptyException;
import org.example.stockms.exception.StoreNotFoundException;
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

    @Override
    public List<Store> saveStore(Integer cantidadStores) {
        List<Store> stores = new ArrayList<>();                 // Creamos una lista vacía para almacenar los objetos Store que vamos a crear
        long totalExistentes = storeRepository.count();         // Obtenemos el número total de stores que ya existen en

        for (int i = 1; i <= cantidadStores; i++) {             // Iteramos desde 1 hasta la cantidad de stores que queremos crear
            Store store = new Store();                          // Creamos un nuevo objeto Store
            store.setName("store" + (totalExistentes + i));     // Asignamos un nombre único al store, continuando la numeración de los existentes
            store.setCapacity(0);                               // Inicializamos la capacidad ocupada del store en 0
            store.setCapacityTotal(100);                        // Definimos la capacidad máxima del store en 100
            stores.add(store);                                  // Añadimos el store creado a la lista
        }

        return storeRepository.saveAll(stores);                 // Guardamos todos los stores de la lista en la base de datos en una sola operación y devolvemos la lista de stores guardados
    }

    @Override
    public List<Store> findAllStores() {
        return storeRepository.findAll();                       // Consultamos y devolvemos todos los registros de stores existentes en la base de datos
    }

    @Override
    public Store findStoreById(Long id) {
        // Buscamos un store por su ID en la base de datos; si no existe, lanzamos la excepción personalizada StoreNotFoundException
        return storeRepository.findById(id)
                .orElseThrow(()-> new StoreNotFoundException(id));
    }

    @Override
    public void deleteStoreById(Long id) {
        // Buscamos el store por ID; si no existe, lanzamos StoreNotFoundException
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException(id));

        // Verificamos si el store aún tiene stock; si es así, lanzamos StoreNotEmptyException
        if (store.getCapacity() > 0) {
            throw new StoreNotEmptyException(store.getId(),store.getCapacity());
        }

        // Si pasa las validaciones, eliminamos el store por su ID
        storeRepository.deleteById(id);
    }


    @Override
    public Store updateStoreById(Long id, Store store) {
        // Obtenemos el store actual desde la base de datos; si no existe, se lanzará StoreNotFoundException
        Store storeActual = findStoreById(id);

        storeActual.setName(store.getName());                       // Actualizamos el nombre del store
        storeActual.setCapacity(storeActual.getCapacity());         // Mantenemos la capacidad actual sin cambios
        storeActual.setCapacityTotal(store.getCapacityTotal());     // Actualizamos la capacidad máxima del store

        return storeRepository.save(storeActual);                   // Guardamos los cambios en la base de datos y devolvemos el store actualizado
    }

    @Override
    public List<StoreCantidadProductoAlmacenDto> findAllStoreProductosDto() {
        // Obtenemos todos los stores de la base de datos
        return storeRepository.findAll()
                .stream()
                .map(store -> {
                    //Para cada store, procesamos su lista de stocks
                    //    Agrupamos los stocks por productId y calculamos la cantidad neta de cada producto
                    //    - Si el stock es de tipo "entrada", se suma la cantidad
                    //    - Si el stock es de tipo "salida", se resta la cantidad
                    Map<Long, Integer> productosAgrupados = store.getStocks().stream()
                            .collect(Collectors.groupingBy(
                                    Stock::getProductId,    // Agrupamos por el ID del producto
                                    Collectors.summingInt(stock ->
                                            "entrada".equalsIgnoreCase(stock.getState())    // Verificamos si es entrada
                                                    ? stock.getQuantity()                   // sumamos cantidad de entrada
                                                    : -stock.getQuantity()                  // restamos cantidad de salida
                                    )
                            ));

                    // Convertimos el Map resultante en una lista de DTOs
                    // Cada entrada del Map contiene el productId y la cantidad neta disponible
                    List<StoreProductQuantityDto> productos = productosAgrupados.entrySet()
                            .stream()
                            .map(entry -> new StoreProductQuantityDto(
                                    entry.getKey(),                 // productId
                                    entry.getValue().longValue()    // cantidad total de ese producto en el store
                            ))
                            .toList();

                    // Creamos un DTO final que representa al store con su lista de productos y cantidades
                    // StoreCantidadProductoAlmacenDto contiene:
                    // - El ID del store
                    // - La lista de productos con sus cantidades netas
                    return new StoreCantidadProductoAlmacenDto(store.getId().longValue(), productos);
                })
                .toList();// Convertimos el Stream final en una lista y la retornamos
    }
}

package org.example.stockms.service.stock;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.stockms.exception.InvalidStockRequestException;
import org.example.stockms.exception.ProductNotFoundException;
import org.example.stockms.exception.StockInsufficientException;
import org.example.stockms.mapper.StockMapper;
import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.*;
import org.example.stockms.model.store.Store;
import org.example.stockms.repository.StockRepository;
import org.example.stockms.repository.StoreRepository;
import org.example.stockms.service.store.StoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final StockMapper stockMapper;

//    @Override
//    public List<Stock> save(List<StockRequestDto> stockRequestDtos) {
//        // Creamos una lista vacía que contendrá todos los objetos Stock que vamos a guardar
//        List<Stock> allStocksToSave = new ArrayList<>();
//
//        // Iteramos sobre cada DTO recibido para procesar cada solicitud de stock
//        for (StockRequestDto stockRequestDto : stockRequestDtos) {
//            // Guardamos la cantidad que necesitamos almacenar del producto actual
//            int quantityToStore = stockRequestDto.getQuantity();
//
//            // Intentamos obtener el almacén especificado en el DTO
//            // Si no existe, se crea uno nuevo con nombre basado en el ID, capacidad inicial 0 y capacidad máxima 100
//            Store store = storeRepository.findById(stockRequestDto.getStoreId())
//                    .orElseGet(() -> {
//                        Store newStore = new Store();
//                        newStore.setName("store" + stockRequestDto.getStoreId());
//                        newStore.setCapacity(0);
//                        newStore.setCapacityTotal(100);
//                        return storeRepository.save(newStore);
//                    });
//
//            // Obtenemos la lista completa de almacenes existentes y los ordenamos por ID
//            List<Store> stores = storeService.findAllStores()
//                    .stream()
//                    .sorted(Comparator.comparing(Store::getId))
//                    .toList();
//
//            // Determinamos la posición del almacén inicial dentro de la lista ordenada
//            int index = stores.indexOf(store);
//            if (index == -1) {
//                // Si el almacén no se encuentra en la lista (caso raro), lo añadimos y recalculamos el índice
//                stores = new ArrayList<>(stores);
//                stores.add(store);
//                stores.sort(Comparator.comparing(Store::getId));
//                index = stores.indexOf(store);
//            }
//
//            // Mientras haya cantidad pendiente por almacenar, intentamos ubicarla en los almacenes disponibles
//            while (quantityToStore > 0) {
//                // Si ya no quedan almacenes disponibles, creamos uno nuevo automáticamente
//                if (index >= stores.size()) {
//                    Store newStore = new Store();
//                    newStore.setName("store" + (stores.size() + 1)); // nombre secuencial
//                    newStore.setCapacity(0);
//                    newStore.setCapacityTotal(100);
//                    storeRepository.save(newStore);                 // guardamos el nuevo store
//
//                    // Actualizamos la lista de stores para incluir el nuevo
//                    stores = new ArrayList<>(stores);
//                    stores.add(newStore);
//                }
//
//                // Tomamos el almacén actual para intentar almacenar allí la cantidad pendiente
//                Store currentStore = stores.get(index);
//
//                // Calculamos el espacio disponible en el almacén
//                int freeSpace = currentStore.getCapacityTotal() - currentStore.getCapacity();
//
//                // Determinamos cuánto podemos almacenar en este almacén (lo mínimo entre lo que falta y el espacio disponible)
//                int toPut = Math.min(quantityToStore, freeSpace);
//
//                // Si hay espacio, creamos un registro de Stock para la cantidad que se puede almacenar
//                if (toPut > 0) {
//                    Stock saved = new Stock();
//                    saved.setProductId(stockRequestDto.getProductId());         // ID del producto
//                    saved.setQuantity(toPut);                                   // cantidad que se va a almacenar en este almacén
//                    saved.setState("entrada");                                  // estado de movimiento: entrada
//                    saved.setStore(currentStore);                               // asignamos el almacén donde se guarda
//
//                    allStocksToSave.add(saved);                                 // añadimos el stock a la lista de stocks a guardar
//
//                    // Actualizamos la capacidad ocupada del almacén
//                    currentStore.setCapacity(currentStore.getCapacity() + toPut);
//                    storeRepository.save(currentStore);// guardamos el store actualizado
//
//                    // Reducimos la cantidad pendiente de almacenar
//                    quantityToStore -= toPut;
//                }
//
//                // Pasamos al siguiente almacén en la lista
//                index++;
//            }
//        }
//
//        // Guardamos todos los registros de stock creados en la base de datos en una sola operación y los devolvemos
//        return stockRepository.saveAll(allStocksToSave);
//    }

    @Override
    @Transactional
    public List<Stock> save(List<StockRequestDto> stockRequestDtos) {
        List<Stock> allStocksToSave = new ArrayList<>();

        for (StockRequestDto stockRequestDto : stockRequestDtos) {

            // -----------------------------
            // 🔹 Validaciones iniciales
            // -----------------------------
            if (stockRequestDto.getQuantity() == null || stockRequestDto.getQuantity() <= 0) {
                throw new InvalidStockRequestException(
                        "Cantidad inválida para el producto " + stockRequestDto.getProductId()
                                + ". Debe ser mayor a 0."
                );
            }
            if (stockRequestDto.getProductId() == null || stockRequestDto.getProductId() <= 0) {
                throw new InvalidStockRequestException(
                        "Producto inválido: " + stockRequestDto.getProductId()
                );
            }
            if (stockRequestDto.getStoreId() == null || stockRequestDto.getStoreId() <= 0) {
                throw new InvalidStockRequestException(
                        "StoreId inválido: " + stockRequestDto.getStoreId()
                );
            }

            int quantityToStore = stockRequestDto.getQuantity();

            Store store = storeRepository.findById(stockRequestDto.getStoreId())
                    .orElseGet(() -> {
                        Store newStore = new Store();
                        newStore.setName("store" + stockRequestDto.getStoreId());
                        newStore.setCapacity(0);
                        newStore.setCapacityTotal(100);
                        return storeRepository.save(newStore);
                    });

            List<Store> stores = storeService.findAllStores()
                    .stream()
                    .sorted(Comparator.comparing(Store::getId))
                    .toList();

            int index = stores.indexOf(store);
            if (index == -1) {
                stores = new ArrayList<>(stores);
                stores.add(store);
                stores.sort(Comparator.comparing(Store::getId));
                index = stores.indexOf(store);
            }

            while (quantityToStore > 0) {
                if (index >= stores.size()) {
                    Store newStore = new Store();
                    newStore.setName("store" + (stores.size() + 1));
                    newStore.setCapacity(0);
                    newStore.setCapacityTotal(100);
                    storeRepository.save(newStore);

                    stores = new ArrayList<>(stores);
                    stores.add(newStore);
                }

                Store currentStore = stores.get(index);
                int freeSpace = currentStore.getCapacityTotal() - currentStore.getCapacity();
                int toPut = Math.min(quantityToStore, freeSpace);

                if (toPut > 0) {
                    Stock saved = new Stock();
                    saved.setProductId(stockRequestDto.getProductId());
                    saved.setQuantity(toPut);
                    saved.setState("entrada");
                    saved.setStore(currentStore);

                    allStocksToSave.add(saved);

                    currentStore.setCapacity(currentStore.getCapacity() + toPut);
                    storeRepository.save(currentStore);

                    quantityToStore -= toPut;
                }

                index++;
            }
        }

        return stockRepository.saveAll(allStocksToSave);
    }

    @Override
    public List<Stock> findAll() {
        // Consultamos y devolvemos todos los registros de stock existentes en la base de datos
        // Cada objeto Stock contiene información del producto, cantidad, estado y el almacén asociado
        return stockRepository.findAll();
    }

    @Override
    public Stock findById(Long id) {
        // Buscamos un stock por su ID en la base de datos
        // Si no se encuentra, lanzamos una excepción genérica con el mensaje "Stock not found"
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    @Override
    public StockFindProductDto findByProductId(Long productId) {
        // Verificamos si existe al menos un stock con el productId dado en cualquier store
        boolean existe = storeService.findAllStores()
                .stream()                                                           // recorremos todos los stores
                .flatMap(store -> store.getStocks().stream())                 // obtenemos todos los stocks de cada store
                .anyMatch(stock -> stock.getProductId().equals(productId));   // verificamos si alguno coincide
        // Si no existe ningún stock para el productId, lanzamos excepción personalizada
        if (!existe) {
                throw new ProductNotFoundException(productId);
        }

        // Obtenemos todos los movimientos (entradas y salidas) del producto en todos los stores
        List<Stock> movimientos = storeService.findAllStores()
                .stream()
                .flatMap(store -> store.getStocks().stream())
                .filter(stock -> stock.getProductId().equals(productId))    // filtramos solo el producto deseado
                .toList();

        // Calculamos el saldo final del producto sumando las entradas y restando las salidas
        int saldo = movimientos.stream()
                .mapToInt(stock -> "entrada".equalsIgnoreCase(stock.getState())
                        ? stock.getQuantity()
                        : -stock.getQuantity())
                .sum();

        // Creamos y retornamos el DTO que contiene:
        // - El ID del producto
        // - La lista de movimientos convertidos a DTOs
        // - El saldo final disponible
        return new StockFindProductDto(
                productId,
                stockMapper.toListStockResponseDto(movimientos),
                saldo
        );
    }

    @Override
    @Transactional
    public List<StockSalidaResponseDto> restarStock(List<StockSalidaRequestDto> solicitudes) {
        // Lista global para acumular las respuestas de cada solicitud de salida
        List<StockSalidaResponseDto> respuestaGlobal = new ArrayList<>();

        // Iteramos sobre cada solicitud de salida de stock
        for (StockSalidaRequestDto solicitud : solicitudes) {
            int restante = solicitud.getQuantity();         // cantidad pendiente de retirar del producto solicitado

            // Obtenemos todas las entradas de este producto en todos los almacenes, ordenadas por fecha (FIFO)
            List<Stock> entradasProducto = storeRepository.findAll().stream()
                    .flatMap(store -> store.getStocks().stream()
                            .filter(stock -> stock.getProductId().equals(solicitud.getProductId())))
                    .sorted(Comparator.comparing(Stock::getDate))       // priorizamos las entradas más antiguas
                    .toList();

            // Recorremos cada entrada para ir retirando stock hasta cubrir la solicitud
            for (Stock entrada : entradasProducto) {
                if (restante <= 0) break;           // ya se retiró todo lo solicitado

                Store store = entrada.getStore();   // almacén asociado a la entrada actual

                // Calculamos la cantidad disponible real de este producto en este almacén
                int entradas = store.getStocks().stream()
                        .filter(s -> s.getProductId().equals(solicitud.getProductId())
                                && s.getState().equalsIgnoreCase("entrada"))
                        .mapToInt(Stock::getQuantity).sum();

                int salidas = store.getStocks().stream()
                        .filter(s -> s.getProductId().equals(solicitud.getProductId())
                                && s.getState().equalsIgnoreCase("salida"))
                        .mapToInt(Stock::getQuantity).sum();


                int disponible = entradas - salidas;
                if (disponible <= 0) continue; // nada disponible en este store → pasar al siguiente

                // Determinar cuánto retirar de este almacén
                int cantidadRetirar = Math.min(disponible, restante);

                // Registrar un nuevo movimiento de SALIDA
                Stock stockSalida = new Stock();
                stockSalida.setQuantity(cantidadRetirar);
                stockSalida.setState("salida");
                stockSalida.setProductId(solicitud.getProductId());
                stockSalida.setStore(store);
                stockRepository.save(stockSalida);
                store.getStocks().add(stockSalida);

                // -----------------------------------------------------
                // Aquí recalculamos la capacidad REAL del store
                // -----------------------------------------------------

                // Total de entradas (todos los productos) en este store
                int entradasStore = store.getStocks().stream()
                        .filter(s -> s.getState().equalsIgnoreCase("entrada"))
                        .mapToInt(Stock::getQuantity)
                        .sum();


                // Total de salidas (todos los productos) en este store
                int salidasStore = store.getStocks().stream()
                        .filter(s -> s.getState().equalsIgnoreCase("salida"))
                        .mapToInt(Stock::getQuantity)
                        .sum();

                // Ocupado = entradas - salidas → lo que realmente hay en el almacén
                int ocupado = entradasStore - salidasStore;

                // Capacidad restante = capacidad total - ocupado
                int capacidadRestante = store.getCapacityTotal() - ocupado;

                // Aquí actualizamos la capacidad del store
                store.setCapacity(ocupado);
                storeRepository.save(store);


                // Actualizamos cuánto falta por retirar
                restante -= cantidadRetirar;

                // Guardamos en la respuesta
                respuestaGlobal.add(new StockSalidaResponseDto(
                        store.getId(),
                        cantidadRetirar,
                        capacidadRestante,
                        solicitud.getProductId()
                ));
            }

            // 🔹 Si no se pudo retirar todo lo solicitado → error
            if (restante > 0) {
//                throw new RuntimeException("No hay suficiente stock del producto "
//                        + solicitud.getProductId() + ". Faltan " + restante + " unidades.");
                throw new StockInsufficientException(solicitud.getProductId(), restante);

            }
        }

        return respuestaGlobal;
    }

}
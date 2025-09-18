package org.example.stockms.service.stock;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    @Override
    public List<Stock> save(List<StockRequestDto> stockRequestDtos) {
        List<Stock> allStocksToSave = new ArrayList<>();

        for (StockRequestDto stockRequestDto : stockRequestDtos) {
            // Guardamos la cantidad de producto que queremos registrar en stock
            int quantityToStore = stockRequestDto.getQuantity();

            // Intentamos obtener el almacén inicial, si no existe creamos uno nuevo
            Store store = storeRepository.findById(stockRequestDto.getStoreId())
                    .orElseGet(() -> {
                        Store newStore = new Store();
                        newStore.setName("store" + stockRequestDto.getStoreId());
                        newStore.setCapacity(0);
                        newStore.setCapacityTotal(100);
                        return storeRepository.save(newStore);
                    });

            // Obtenemos todos los almacenes, ordenados por ID
            List<Store> stores = storeService.findAllStores()
                    .stream()
                    .sorted(Comparator.comparing(Store::getId))
                    .toList();

            // Obtenemos el índice del almacén inicial dentro de la lista de almacenes
            int index = stores.indexOf(store);
            if (index == -1) {
                // si no está en la lista (caso raro), lo añadimos y ajustamos índice
                stores = new ArrayList<>(stores);
                stores.add(store);
                stores.sort(Comparator.comparing(Store::getId));
                index = stores.indexOf(store);
            }

            // Mientras aún quede cantidad de producto por registrar
            while (quantityToStore > 0) {
                // Si ya no hay almacenes suficientes, creamos uno nuevo automáticamente
                if (index >= stores.size()) {
                    Store newStore = new Store();
                    newStore.setName("store" + (stores.size() + 1));
                    newStore.setCapacity(0);
                    newStore.setCapacityTotal(100);
                    storeRepository.save(newStore);

                    stores = new ArrayList<>(stores);
                    stores.add(newStore);
                }

                // Tomamos el almacén actual en el que vamos a intentar guardar
                Store currentStore = stores.get(index);

                // Calculamos el espacio libre disponible en ese almacén
                int freeSpace = currentStore.getCapacityTotal() - currentStore.getCapacity();

                // Definimos cuánto producto podemos poner (lo mínimo entre lo que falta y el espacio libre)
                int toPut = Math.min(quantityToStore, freeSpace);

                // Si hay espacio, registramos el stock en el almacén actual
                if (toPut > 0) {
                    Stock saved = new Stock();
                    saved.setProductId(stockRequestDto.getProductId());
                    saved.setQuantity(toPut);
                    saved.setState("entrada"); // estado de movimiento: entrada
                    saved.setStore(currentStore);

                    allStocksToSave.add(saved);

                    // Actualizamos la capacidad ocupada del almacén
                    currentStore.setCapacity(currentStore.getCapacity() + toPut);
                    storeRepository.save(currentStore);

                    // Reducimos la cantidad pendiente por almacenar
                    quantityToStore -= toPut;
                }

                // Pasamos al siguiente almacén
                index++;
            }
        }

        // Guardamos todos los registros de stock creados en la base de datos
        return stockRepository.saveAll(allStocksToSave);
    }

    //Devuelve todos los stock
    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    //Busca por id un stock
    @Override
    public Stock findById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }


    @Override
    public StockFindProductDto findByProductId(Long productId) {
        // Buscar si existe algún stock con ese productId
        boolean existe = storeService.findAllStores()
                .stream()
                .flatMap(store -> store.getStocks().stream())
                .anyMatch(stock -> stock.getProductId().equals(productId));

        if (!existe) {
            throw new RuntimeException("No existe stock para el producto con id: " + productId);
        }

        // Obtener todos los movimientos (entradas y salidas)
        List<Stock> movimientos = storeService.findAllStores()
                .stream()
                .flatMap(store -> store.getStocks().stream())
                .filter(stock -> stock.getProductId().equals(productId))
                .toList();

        // Calcular el saldo final
        int saldo = movimientos.stream()
                .mapToInt(stock -> "entrada".equalsIgnoreCase(stock.getState())
                        ? stock.getQuantity()
                        : -stock.getQuantity())
                .sum();

        return new StockFindProductDto(
                productId,
                stockMapper.toListStockResponseDto(movimientos),
                saldo
        );
    }

//    @Override
//    @Transactional
//    public List<StockSalidaResponseDto> restarStock(List<StockSalidaRequestDto> solicitudes) {
//        List<StockSalidaResponseDto> respuestaGlobal = new ArrayList<>();
//
//        for (StockSalidaRequestDto solicitud : solicitudes) {
//            System.out.println(solicitud);
//            int restante = solicitud.getQuantity();
//            List<StockSalidaResponseDto> respuestaParcial = new ArrayList<>();
//
//            // Obtenemos todos los stocks del producto solicitado
//            List<Stock> stocksOrdenados = storeRepository.findAll().stream()
//                    .flatMap(store -> store.getStocks().stream()
//                            .filter(stock -> stock.getProductId().equals(solicitud.getProductId())
//                                    && stock.getState().equalsIgnoreCase("entrada")
//                                    && stock.getQuantity() > 0))
//                    .sorted(Comparator.comparingInt(Stock::getQuantity))
//                    .toList();
//
//            for (Stock stockEntrada : stocksOrdenados) {
//                if (restante <= 0) break;
//
//                Store store = stockEntrada.getStore();
//
//                // Entradas de este producto en este almacén
//                int entradas = store.getStocks().stream()
//                        .filter(s -> s.getProductId().equals(solicitud.getProductId())
//                                && s.getState().equalsIgnoreCase("entrada"))
//                        .mapToInt(Stock::getQuantity)
//                        .sum();
//
//                // Salidas de este producto en este almacén
//                int salidas = store.getStocks().stream()
//                        .filter(s -> s.getProductId().equals(solicitud.getProductId())
//                                && s.getState().equalsIgnoreCase("salida"))
//                        .mapToInt(Stock::getQuantity)
//                        .sum();
//
//                int disponible = entradas - salidas;
//                if (disponible <= 0) continue;
//
//                int cantidadRetirar = Math.min(disponible, restante);
//
//                // Creamos movimiento de salida
//                Stock stockSalida = new Stock();
//                stockSalida.setQuantity(cantidadRetirar);
//                stockSalida.setState("salida");
//                stockSalida.setProductId(stockEntrada.getProductId());
//                stockSalida.setStore(store);
//                stockRepository.save(stockSalida);
//
//                int nuevaCapacidad = entradas - (salidas + cantidadRetirar);
//                store.setCapacity(nuevaCapacidad);
//                storeRepository.save(store);
//
//                restante -= cantidadRetirar;
//
//                respuestaParcial.add(new StockSalidaResponseDto(
//                        store.getId(),
//                        cantidadRetirar,
//                        nuevaCapacidad,
//                        stockEntrada.getProductId()
//                ));
//            }
//
//            if (restante > 0) {
//                throw new RuntimeException("No hay suficiente stock del producto "
//                        + solicitud.getProductId() + ". Faltan " + restante + " unidades.");
//            }
//
//            respuestaGlobal.addAll(respuestaParcial);
//        }
//
//        return respuestaGlobal;
//    }

    @Override
    @Transactional
    public List<StockSalidaResponseDto> restarStock(List<StockSalidaRequestDto> solicitudes) {
        List<StockSalidaResponseDto> respuestaGlobal = new ArrayList<>();

        for (StockSalidaRequestDto solicitud : solicitudes) {
            int restante = solicitud.getQuantity();

            // 🔹 Buscar todos los stocks de entrada del producto ordenados por fecha (FIFO)
            List<Stock> entradasProducto = storeRepository.findAll().stream()
                    .flatMap(store -> store.getStocks().stream()
                            .filter(stock -> stock.getProductId().equals(solicitud.getProductId())
                                    && stock.getState().equalsIgnoreCase("entrada")))
                    .sorted(Comparator.comparing(Stock::getDate)) // más antiguo primero
                    .toList();

            for (Stock entrada : entradasProducto) {
                if (restante <= 0) break;

                Store store = entrada.getStore();

                // calcular stock disponible real en este store
                int entradas = store.getStocks().stream()
                        .filter(s -> s.getProductId().equals(solicitud.getProductId())
                                && s.getState().equalsIgnoreCase("entrada"))
                        .mapToInt(Stock::getQuantity).sum();

                int salidas = store.getStocks().stream()
                        .filter(s -> s.getProductId().equals(solicitud.getProductId())
                                && s.getState().equalsIgnoreCase("salida"))
                        .mapToInt(Stock::getQuantity).sum();

                int disponible = entradas - salidas;
                if (disponible <= 0) continue;

                int cantidadRetirar = Math.min(disponible, restante);

                // registrar salida
                Stock stockSalida = new Stock();
                stockSalida.setQuantity(cantidadRetirar);
                stockSalida.setState("salida");
                stockSalida.setProductId(solicitud.getProductId());
                stockSalida.setStore(store);
                stockRepository.save(stockSalida);

                // 🔹 recalcular capacidad ocupada total en el store (todos los productos)
                int entradasStore = store.getStocks().stream()
                        .filter(s -> s.getState().equalsIgnoreCase("entrada"))
                        .mapToInt(Stock::getQuantity)
                        .sum();

                int salidasStore = store.getStocks().stream()
                        .filter(s -> s.getState().equalsIgnoreCase("salida"))
                        .mapToInt(Stock::getQuantity)
                        .sum();

                int ocupado = entradasStore - salidasStore;
                int capacidadRestante = store.getCapacityTotal() - ocupado;

                store.setCapacity(ocupado);
                storeRepository.save(store);

                restante -= cantidadRetirar;

                respuestaGlobal.add(new StockSalidaResponseDto(
                        store.getId(),
                        cantidadRetirar,
                        capacidadRestante,
                        solicitud.getProductId()
                ));
            }

            if (restante > 0) {
                throw new RuntimeException("No hay suficiente stock del producto "
                        + solicitud.getProductId() + ". Faltan " + restante + " unidades.");
            }
        }

        return respuestaGlobal;
    }


//----------------------


    @Override
    public Stock updateById(Long id, StockRequestDto stockRequestDto) {
        Stock stock = findById(id);

        // Solo actualizamos los campos que vengan no nulos
        if (stockRequestDto.getStoreId() != null) {
            stock.setStore(storeService.findStoreById(stockRequestDto.getStoreId()));
        }

        if (stockRequestDto.getQuantity() != null) {
            stock.setQuantity(stockRequestDto.getQuantity());
        }

        if (stockRequestDto.getProductId() != null) {
            stock.setProductId(stockRequestDto.getProductId());
        }

        stock.setState("entrada");

        return stockRepository.save(stock);
    }



    @Override
    public void deleteById (Long id){
        Stock stockActual = findById(id);
        List<Store> stores= storeService.findAllStores();

        Store storeActual = stores.stream()
                .filter(s -> s.getStocks().stream()
                        .anyMatch(stock -> stock.getId().equals(id)))
                .findFirst().orElseThrow(() -> new RuntimeException("No se encontro el stock"));

        storeActual.setCapacity(storeActual.getCapacity() - stockActual.getQuantity());

        storeRepository.save(storeActual);

        stockRepository.deleteById(id);
    }

}
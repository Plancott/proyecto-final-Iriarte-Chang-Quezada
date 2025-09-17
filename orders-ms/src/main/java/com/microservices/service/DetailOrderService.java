package com.microservices.service;

import com.microservices.model.entity.DetailOrder;

import java.util.List;

public interface DetailOrderService {
    List<DetailOrder> listForOrder(Long id);
    DetailOrder findById(Long id);
    DetailOrder save(DetailOrder detailOrder);
    void delete(Long id);

}

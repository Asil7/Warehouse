package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.order.OrderProductDto;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.OrderProductRepository;
import com.example.demo.repository.WarehouseRepository;

// import jakarta.transaction.Transactional;

@Service
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public ApiResponse editOrderProduct(Long id, OrderProductDto orderProductDto) {

        Optional<OrderProduct> existingOrderProduct = orderProductRepository.findById(id);

        if (existingOrderProduct.isEmpty()) {
            return new ApiResponse("Order product not found", false);
        }

        OrderProduct orderProduct = existingOrderProduct.get();

        Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(orderProduct.getProduct());

        if (existingWarehouseProduct.isEmpty()) {
            return new ApiResponse("Warehouse product not found", false);
        }

        Warehouse warehouse = existingWarehouseProduct.get();

        Long quantityDifference = orderProductDto.getQuantity() - orderProduct.getQuantity();

        warehouse.setQuantity(warehouse.getQuantity() - quantityDifference);

        warehouseRepository.save(warehouse);

        orderProduct.setQuantity(orderProductDto.getQuantity());
        orderProduct.setType(orderProductDto.getType());

        orderProductRepository.save(orderProduct);

        return new ApiResponse("Order product updated", true);
    }

    public ApiResponse deleteOrderProduct(Long id) {
        Optional<OrderProduct> existingOrderProduct = orderProductRepository.findById(id);

        if (existingOrderProduct.isEmpty()) {
            return new ApiResponse("Order product not found", false);
        }

        OrderProduct orderProduct = existingOrderProduct.get();

        Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(orderProduct.getProduct());

        Warehouse warehouse = existingWarehouseProduct.get();

        warehouse.setQuantity(warehouse.getQuantity() + orderProduct.getQuantity());

        warehouseRepository.save(warehouse);

        orderProductRepository.delete(orderProduct);

        return new ApiResponse("Order product deleted", true);
    }
}

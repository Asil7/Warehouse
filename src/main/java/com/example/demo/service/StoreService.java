package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.entity.Store;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public ApiResponse createStoreProduct(StoreDto storeDto) {
        Optional<Warehouse> existingWarehouseProduct = warehouseRepository
                .findByProduct(storeDto.getProduct());

        if (existingWarehouseProduct.isPresent()) {
            Warehouse warehouse = existingWarehouseProduct.get();
            warehouse.setQuantity(warehouse.getQuantity() + storeDto.getQuantity());
            warehouseRepository.save(warehouse);
        } else {
            Warehouse newWarehouseProduct = new Warehouse(
                    storeDto.getProduct(),
                    storeDto.getQuantity(),
                    storeDto.getType());
            warehouseRepository.save(newWarehouseProduct);
        }

        Store newStore = new Store(
                storeDto.getProduct(),
                storeDto.getQuantity(),
                storeDto.getPrice(),
                storeDto.getType());
        storeRepository.save(newStore);

        return new ApiResponse("Store created", true);
    }

    public ApiResponse getAllStoreProducts() {
        List<Store> storeProductsList = storeRepository.findAll();
        return new ApiResponse("Store Products List", true, storeProductsList);
    }
}

package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.entity.Store;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.StoreRepository;

@Service
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    public ApiResponse addProduct(StoreDto storeDto) {

        if (storeRepository.existsByProduct(storeDto.getProduct())) {
            return new ApiResponse("Product with the name " + storeDto.getProduct() + " already exists.", false);
        }

        Store store = new Store();

        store.setProduct(storeDto.getProduct());
        store.setQuantity(storeDto.getQuantity());
        store.setPrice(storeDto.getPrice());
        store.setType(storeDto.getType());
        store.setReceived(false);
        store.setPaid(false);

        storeRepository.save(store);

        return new ApiResponse("Product added", true);
    }

    public ApiResponse getStoreProductList() {
        List<Store> storeProductList = storeRepository.findAll();
        return new ApiResponse("Store Product List", true, storeProductList);
    }

}

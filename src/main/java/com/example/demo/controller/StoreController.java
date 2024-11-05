package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.StoreService;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @PostMapping
    public HttpEntity<?> addStoreProduct(@RequestBody StoreDto storeDto) {
        ApiResponse apiResponse = storeService.addProduct(storeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @PutMapping("/{id}")
    public HttpEntity<?> updateStoreProduct(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        ApiResponse apiResponse = storeService.updateProduct(id, storeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getStoreProductList() {
        ApiResponse apiResponse = storeService.getStoreProductList();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @PutMapping("/{id}/paid")
    public HttpEntity<?> updateStorePaidStatus(@PathVariable Long id, @RequestParam boolean paid) {
    	ApiResponse apiResponse = storeService.updateStorePaidStatus(id, paid);
    	return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @PutMapping("/{id}/received")
    public HttpEntity<?> updateStoreReceivedStatus(@PathVariable Long id, @RequestBody StoreDto storeDto) {
    	ApiResponse apiResponse = storeService.updateStoreReceivedStatus(id, storeDto);
    	return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

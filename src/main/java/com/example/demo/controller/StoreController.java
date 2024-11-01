package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PreAuthorize("hasAuthority('ADD_STORE_PRODUCT')")
    @PostMapping
    public HttpEntity<?> createStoreProduct(@RequestBody StoreDto storeDto) {
        ApiResponse apiResponse = storeService.createStoreProduct(storeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @PutMapping("/{id}")
    public HttpEntity<?> updateStoreProduct(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        ApiResponse apiResponse = storeService.updateStoreProduct(id, storeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAuthority('VIEW_STORE_PRODUCT_LIST')")
    @GetMapping
    public HttpEntity<?> getStoreProducts() {
        ApiResponse apiResponse = storeService.getAllStoreProducts();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteStoreProduct(@PathVariable Long id) {
    	ApiResponse apiResponse = storeService.deleteStoreProduct(id);
    	return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

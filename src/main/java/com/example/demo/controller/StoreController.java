package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.StoreService;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    StoreService storeService;

    @PostMapping
    public HttpEntity<?> addStoreProduct(@RequestBody StoreDto storeDto) {
        try {
            ApiResponse apiResponse = storeService.addProduct(storeDto);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error adding store product: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to add product", false));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateStoreProduct(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        try {
            ApiResponse apiResponse = storeService.updateProduct(id, storeDto);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error updating store product with ID '{}': ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to update product", false));
        }
    }

    @GetMapping
    public HttpEntity<?> getStoreProductList() {
        try {
            ApiResponse apiResponse = storeService.getStoreProductList();
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error fetching store product list: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to fetch product list", false));
        }
    }

    @GetMapping("/warehouse")
    public HttpEntity<?> addWarehouseProduct() {
        try {
            ApiResponse apiResponse = storeService.addProductFromWarehouse();
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error adding warehouse products to store: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to add warehouse products", false));
        }
    }

    @PutMapping("/{id}/paid")
    public HttpEntity<?> updateStorePaidStatus(@PathVariable Long id, @RequestParam boolean paid) {
        try {
            ApiResponse apiResponse = storeService.updateStorePaidStatus(id, paid);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error updating paid status for store product with ID '{}': ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to update paid status", false));
        }
    }

    @PutMapping("/{id}/received")
    public HttpEntity<?> updateStoreReceivedStatus(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        try {
            ApiResponse apiResponse = storeService.updateStoreReceivedStatus(id, storeDto);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error updating received status for store product with ID '{}': ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to update received status", false));
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteStoreProduct(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = storeService.deleteProduct(id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error deleting store product with ID '{}': ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to delete product", false));
        }
    }
}

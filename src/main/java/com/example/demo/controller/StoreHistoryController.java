package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.StoreHistoryService;

@RestController
@RequestMapping("/api/store-history")
public class StoreHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(StoreHistoryController.class);

    @Autowired
    StoreHistoryService storeService;

    @PreAuthorize("hasAuthority('ADD_STORE_PRODUCT')")
    @PostMapping
    public HttpEntity<?> createStoreProduct(@RequestBody StoreDto storeDto) {
        try {
            ApiResponse apiResponse = storeService.createStoreProduct(storeDto);
            logger.info("Store product created successfully");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error in createStoreProduct: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateStoreProduct(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        try {
            ApiResponse apiResponse = storeService.updateStoreProduct(id, storeDto);
            logger.info("Store product updated successfully with ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error in updateStoreProduct for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }

    @PreAuthorize("hasAuthority('VIEW_STORE_PRODUCT_LIST')")
    @GetMapping
    public HttpEntity<?> getStoreProducts() {
        try {
            ApiResponse apiResponse = storeService.getAllStoreProducts();
            logger.info("Retrieved store products successfully");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error in getStoreProducts: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteStoreProduct(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = storeService.deleteStoreProduct(id);
            logger.info("Store product deleted successfully with ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error in deleteStoreProduct for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }

    @PutMapping("/{id}/paid")
    public HttpEntity<?> updateStoreHistoryPaidStatus(@PathVariable Long id, @RequestParam boolean paid) {
        try {
            ApiResponse apiResponse = storeService.updateStoreHistoryPaidStatus(id, paid);
            logger.info("Paid status updated successfully for store product with ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error in updateStoreHistoryPaidStatus for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }
}

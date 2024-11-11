package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.demo.dto.product.ProductsReceiptDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ProductsReceiptService;

@RestController
@RequestMapping("/api/products-receipt")
public class ProductsReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(ProductsReceiptController.class);

    @Autowired
    ProductsReceiptService productsReceiptService;

    @PreAuthorize("hasAuthority('ADD_PRODUCT_RECEIPT')")
    @PostMapping
    public HttpEntity<?> createProductsReceipt(@RequestBody ProductsReceiptDto productsReceiptDto) {
        try {
            ApiResponse apiResponse = productsReceiptService.createProductsReceipt(productsReceiptDto);
            logger.info("Product receipt created successfully.");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error creating product receipt: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while creating product receipt", false));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateReceivedProduct(@PathVariable Long id,
                                               @RequestBody ProductsReceiptDto productsReceiptDto) {
        try {
            ApiResponse apiResponse = productsReceiptService.updateReceivedProduct(id, productsReceiptDto);
            logger.info("Product receipt updated successfully for ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error updating product receipt for ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while updating product receipt", false));
        }
    }

    @PreAuthorize("hasAuthority('VIEW_RECEIVED_PRODUCT_LIST')")
    @GetMapping
    public HttpEntity<?> getReceivedProducts() {
        try {
            ApiResponse apiResponse = productsReceiptService.getAllReceivedProducts();
            logger.info("Fetch all received products.");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error get received products: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while retrieving received products", false));
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = productsReceiptService.deleteProduct(id);
            logger.info("Product receipt deleted successfully for ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error deleting product receipt for ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while deleting product receipt", false));
        }
    }
}

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.product.ProductDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @PreAuthorize("hasAuthority('ADD_PRODUCT')")
    @PostMapping
    public HttpEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        try {
            ApiResponse apiResponse = productService.createProduct(productDto);
            logger.info("Product created successfully.");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error creating product: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while creating the product", false));
        }
    }

//    @PreAuthorize("hasAuthority('EDIT_PRODUCT')")
    @PutMapping("/{id}")
    public HttpEntity<?> editProduct(@Valid @PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            ApiResponse apiResponse = productService.editProduct(id, productDto);
            logger.info("Product edited successfully for ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error editing product for ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while editing the product", false));
        }
    }

    @PreAuthorize("hasAuthority('VIEW_PRODUCT_LIST')")
    @GetMapping
    public HttpEntity<?> getProduct() {
        try {
            ApiResponse apiResponse = productService.getAllProducts();
            logger.info("Retrieved all products.");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error retrieving products: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while retrieving products", false));
        }
    }

//    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = productService.deleteProduct(id);
            logger.info("Product deleted successfully for ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error deleting product for ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while deleting the product", false));
        }
    }
}

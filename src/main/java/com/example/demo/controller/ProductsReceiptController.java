package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dto.product.ProductsReceiptDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ProductsReceiptService;

@RestController
@RequestMapping("/api/products-receipt")
public class ProductsReceiptController {

    @Autowired
    ProductsReceiptService productsReceiptService;

    @PostMapping("/create")
    public HttpEntity<?> createProductsReceipt(@RequestBody ProductsReceiptDto productsReceiptDto) {
        ApiResponse apiResponse = productsReceiptService.createProductsReceipt(productsReceiptDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getReceivedProducts() {
        ApiResponse apiResponse = productsReceiptService.getAllReceivedProducts();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

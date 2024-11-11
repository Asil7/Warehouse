package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CreateOrderProductRequest;
import com.example.demo.dto.order.OrderProductDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.OrderProductService;

@RestController
@RequestMapping("/api/order-product")
public class OrderProductController {

    private static final Logger logger = LoggerFactory.getLogger(OrderProductController.class);

    @Autowired
    OrderProductService orderProductService;

    @PreAuthorize("hasAuthority('ADD_ORDER_PRODUCT')")
    @PostMapping("/{id}")
    public HttpEntity<?> createOrderProduct(@PathVariable Long id, @RequestBody CreateOrderProductRequest request) {
        try {
            ApiResponse apiResponse = orderProductService.createOrderProduct(id, request.getProductList());
            logger.info("Order products created successfully for Order ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error creating order products for Order ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while creating the order products", false));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editOrderProduct(@PathVariable Long id, @RequestBody OrderProductDto orderProductDto) {
        try {
            ApiResponse apiResponse = orderProductService.editOrderProduct(id, orderProductDto);
            logger.info("Order product edited successfully for Order ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error editing order product for Order ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while editing the order product", false));
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOrderProduct(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = orderProductService.deleteOrderProduct(id);
            logger.info("Order product deleted successfully for Order ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error deleting order product for Order ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while deleting the order product", false));
        }
    }
}

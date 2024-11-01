package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CreateOrderProductRequest;
import com.example.demo.dto.order.OrderProductDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.OrderProductService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/order-product")
public class OrderProductController {

    @Autowired
    OrderProductService orderProductService;

    @PreAuthorize("hasAuthority('ADD_ORDER_PRODUCT')")
    @PostMapping("/{id}")
    public HttpEntity<?> createOrderProduct(@PathVariable Long id, @RequestBody CreateOrderProductRequest request){
    	ApiResponse apiResponse = orderProductService.createOrderProduct(id, request.getProductList());
    	return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @PutMapping("/{id}")
    public HttpEntity<?> editOrderProduct(@PathVariable Long id, @RequestBody OrderProductDto orderProductDto) {
        ApiResponse apiResponse = orderProductService.editOrderProduct(id, orderProductDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOrderProduct(@PathVariable Long id) {
        ApiResponse apiResponse = orderProductService.deleteOrderProduct(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

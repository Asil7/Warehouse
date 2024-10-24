package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.OrderDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.OrderService;


@RestController
@RequestMapping("/api/order")
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @PostMapping
    public HttpEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        ApiResponse apiResponse = orderService.createOrder(orderDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @GetMapping
    public HttpEntity<?> getOrderList() {
        ApiResponse apiResponse = orderService.getAllOrders();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @GetMapping("/{id}")
    public HttpEntity<?> getOrderById(@PathVariable Long id) {
        ApiResponse apiResponse = orderService.getOrderById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @GetMapping("/products/{orderId}")
    public HttpEntity<?> getOrderProductsByOrderId(@PathVariable Long orderId) {
        ApiResponse apiResponse = orderService.getOrderProductsByOrderId(orderId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    
    @DeleteMapping("/{orderId}")
    public HttpEntity<?> deleteOrder(@PathVariable Long orderId){
    	ApiResponse apiResponse = orderService.deleteOrder(orderId);
    	return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

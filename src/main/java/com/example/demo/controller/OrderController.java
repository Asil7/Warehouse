package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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
}

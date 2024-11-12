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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.OrderDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAuthority('ADD_ORDER')")
    @PostMapping
    public HttpEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        try {
            ApiResponse apiResponse = orderService.createOrder(orderDto);
            logger.info("Order created successfully: {}", orderDto);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error creating order: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while creating the order", false));
        }
    }

    @GetMapping
    public HttpEntity<?> getOrderList() {
        try {
            ApiResponse apiResponse = orderService.getAllOrders();
            logger.info("Fetched all orders");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error fetching orders: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while fetching the orders", false));
        }
    }

    @PreAuthorize("hasAuthority('VIEW_ORDER_LIST_BY_USER')")
    @GetMapping("/user")
    public HttpEntity<?> getOrderListByUsername(@RequestParam String username) {
        try {
            ApiResponse apiResponse = orderService.getOrdersByUsername(username);
            logger.info("Fetched orders for username: {}", username);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error fetching orders for username {}: ", username, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while fetching the orders for the user", false));
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOrderById(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = orderService.getOrderById(id);
            logger.info("Fetched order by ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error fetching order by ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while fetching the order by ID", false));
        }
    }

    @PreAuthorize("hasAuthority('VIEW_ORDER_PRODUCT_LIST')")
    @GetMapping("/products/{orderId}")
    public HttpEntity<?> getOrderProductsByOrderId(@PathVariable Long orderId) {
        try {
            ApiResponse apiResponse = orderService.getOrderProductsByOrderId(orderId);
            logger.info("Fetched products for order ID: {}", orderId);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error fetching products for order ID {}: ", orderId, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while fetching the products for the order", false));
        }
    }

    @DeleteMapping("/{orderId}")
    public HttpEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            ApiResponse apiResponse = orderService.deleteOrder(orderId);
            logger.info("Order deleted successfully for Order ID: {}", orderId);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error deleting order for Order ID {}: ", orderId, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while deleting the order", false));
        }
    }

    @PutMapping("/{id}/delivered")
    public HttpEntity<?> updateOrderDeliveredStatus(@PathVariable Long id, @RequestParam boolean delivered) {
        try {
            ApiResponse apiResponse = orderService.updateOrderDeliveredStatus(id, delivered);
            logger.info("Order delivered status updated for Order ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error updating delivered status for Order ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while updating the order delivered status", false));
        }
    }
    
    @PostMapping("/send")
    public HttpEntity<?> sendNotification(@RequestParam String userToken, 
            @RequestParam Long orderId) {
        try {
            ApiResponse apiResponse = orderService.sendNotification(userToken, orderId);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error send notification: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("Error send notification", false));
        }
    }

}

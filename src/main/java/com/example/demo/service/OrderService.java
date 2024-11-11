package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.OrderProjection;
import com.example.demo.entity.Company;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.OrderProductRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.WarehouseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public ApiResponse createOrder(OrderDto orderDto) {

        try {
            Company company = companyRepository.findById(orderDto.getCompanyId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));

            Order order = new Order();
            order.setUsername(orderDto.getUsername());
            order.setDelivered(false);
            order.setCompany(company);

            List<OrderProduct> productList = orderDto.getProductList().stream().map(dto -> {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(dto.getProduct());
                orderProduct.setQuantity(dto.getQuantity());
                orderProduct.setType(dto.getType());
                orderProduct.setOrder(order);
                return orderProduct;
            }).collect(Collectors.toList());

            order.setProductList(productList);
            orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", order.getId());

            double totalWeight = orderProductRepository.findTotalQuantityByOrderId(order.getId());
            order.setTotalWeight(totalWeight);

            for (OrderProduct orderProduct : productList) {
                Warehouse warehouseProduct = warehouseRepository.findByProduct(orderProduct.getProduct())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Product " + orderProduct.getProduct() + " not found in warehouse"));

                warehouseProduct.setQuantity(warehouseProduct.getQuantity() - orderProduct.getQuantity());
                warehouseRepository.save(warehouseProduct);
            }

            return new ApiResponse("Order created successfully", true, order);

        } catch (Exception e) {
            logger.error("Error creating order for user {}: {}", orderDto.getUsername(), e.getMessage(), e);
            return new ApiResponse("Error creating order", false);
        }
    }

    public ApiResponse getAllOrders() {

        try {
            List<OrderProjection> findAllOrders = orderRepository.findAllOrders();
            logger.info("Fetched {} orders", findAllOrders.size());
            return new ApiResponse("Order List", true, findAllOrders);

        } catch (Exception e) {
            logger.error("Error fetching all orders: {}", e.getMessage(), e);
            return new ApiResponse("Error fetching all orders", false);
        }
    }

    public ApiResponse getOrdersByUsername(String username) {

        try {
            List<OrderProjection> orders = orderRepository.findOrdersByUsername(username);

            if (orders.isEmpty()) {
                logger.info("No orders found for username: {}", username);
                return new ApiResponse("No orders found for username: " + username, true, orders);
            }
            return new ApiResponse("Orders for username: " + username, true, orders);

        } catch (Exception e) {
            logger.error("Error fetching orders for username {}: {}", username, e.getMessage(), e);
            return new ApiResponse("Error fetching orders for username: " + username, false);
        }
    }

    public ApiResponse getOrderById(Long id) {

        try {
            OrderProjection orderById = orderRepository.findOrderById(id);
            logger.info("Fetching order with ID: {}", id);
            return new ApiResponse("Order by id", true, orderById);

        } catch (Exception e) {
            logger.error("Error fetching order by ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error fetching order by id", false);
        }
    }

    public ApiResponse getOrderProductsByOrderId(Long orderId) {

        try {
            List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdSorted(orderId);
            logger.info("Fetching products for order ID: {}", orderId);
            return new ApiResponse("Order Products List", true, orderProducts);

        } catch (Exception e) {
            logger.error("Error fetching products for order ID {}: {}", orderId, e.getMessage(), e);
            return new ApiResponse("Error fetching order products", false);
        }
    }

    public ApiResponse deleteOrder(Long orderId) {
        logger.info("Deleting order with ID: {}", orderId);

        try {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                logger.warn("Order not found with ID: {}", orderId);
                return new ApiResponse("Order not found", false);
            }

            List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdSorted(orderId);
            for (OrderProduct orderProduct : orderProducts) {
                Optional<Warehouse> existingWarehouseProduct = warehouseRepository
                        .findByProduct(orderProduct.getProduct());

                if (existingWarehouseProduct.isPresent()) {
                    Warehouse warehouseProduct = existingWarehouseProduct.get();
                    warehouseProduct.setQuantity(warehouseProduct.getQuantity() + orderProduct.getQuantity());
                    warehouseRepository.save(warehouseProduct);
                } else {
                    logger.warn("Product {} not found in warehouse while deleting order ID {}", 
                                orderProduct.getProduct(), orderId);
                    return new ApiResponse("Product " + orderProduct.getProduct() + " not found in warehouse", false);
                }
            }

            orderProductRepository.deleteAll(orderProducts);
            orderRepository.deleteById(orderId);
            logger.info("Order deleted with ID: {}", orderId);

            return new ApiResponse("Order deleted", true);

        } catch (Exception e) {
            logger.error("Error deleting order with ID {}: {}", orderId, e.getMessage(), e);
            return new ApiResponse("Error deleting order", false);
        }
    }

    public ApiResponse updateOrderDeliveredStatus(Long id, boolean delivered) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);
            if (optionalOrder.isEmpty()) {
                logger.warn("Order not found with ID: {}", id);
                return new ApiResponse("Order not found", false);
            }

            Order order = optionalOrder.get();
            order.setDelivered(delivered);
            orderRepository.save(order);
            logger.info("Order delivery status updated for order ID: {}", id);

            return new ApiResponse("Order delivery status updated", true, order);

        } catch (Exception e) {
            logger.error("Error updating delivery status for order ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error updating order delivery status", false);
        }
    }
}

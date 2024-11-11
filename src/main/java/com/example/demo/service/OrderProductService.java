package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.order.OrderProductDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.OrderProductRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.WarehouseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderProductService {

    private static final Logger logger = LoggerFactory.getLogger(OrderProductService.class);

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    OrderRepository orderRepository;

    public ApiResponse createOrderProduct(Long orderId, List<OrderProductDto> productList) {

        try {
            Optional<Order> existingOrder = orderRepository.findById(orderId);

            if (existingOrder.isEmpty()) {
                logger.warn("Order not found with ID: {}", orderId);
                return new ApiResponse("Order not found", false);
            }

            Order order = existingOrder.get();
            List<OrderProduct> orderProducts = new ArrayList<>();
            double addedWeight = 0.0;

            for (OrderProductDto dto : productList) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(dto.getProduct());
                orderProduct.setQuantity(dto.getQuantity());
                orderProduct.setType(dto.getType());
                orderProduct.setOrder(order);

                Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(dto.getProduct());
                if (existingWarehouseProduct.isPresent()) {
                    Warehouse warehouse = existingWarehouseProduct.get();
                    warehouse.setQuantity(warehouse.getQuantity() - dto.getQuantity());
                    warehouseRepository.save(warehouse);
                } else {
                    logger.warn("Warehouse product not found for product: {}", dto.getProduct());
                    return new ApiResponse("Warehouse product not found for " + dto.getProduct(), false);
                }

                if ("kg".equals(orderProduct.getType()) || "l".equals(orderProduct.getType())) {
                    addedWeight += dto.getQuantity();
                }

                orderProducts.add(orderProduct);
            }

            orderProductRepository.saveAll(orderProducts);
            order.setTotalWeight(order.getTotalWeight() + addedWeight);
            orderRepository.save(order);

            logger.info("Order products created successfully for order ID: {}", orderId);
            return new ApiResponse("Order product created", true);

        } catch (Exception e) {
            logger.error("Error creating order products for order ID {}: {}", orderId, e.getMessage(), e);
            return new ApiResponse("Error creating order products", false);
        }
    }

    public ApiResponse editOrderProduct(Long id, OrderProductDto orderProductDto) {

        try {
            Optional<OrderProduct> existingOrderProduct = orderProductRepository.findById(id);

            if (existingOrderProduct.isEmpty()) {
                logger.warn("Order product not found with ID: {}", id);
                return new ApiResponse("Order product not found", false);
            }

            OrderProduct orderProduct = existingOrderProduct.get();
            Order order = orderProduct.getOrder();
            Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(orderProduct.getProduct());

            if (existingWarehouseProduct.isEmpty()) {
                logger.warn("Warehouse product not found for product: {}", orderProduct.getProduct());
                return new ApiResponse("Warehouse product not found", false);
            }

            Warehouse warehouse = existingWarehouseProduct.get();
            Long quantityDifference = orderProductDto.getQuantity() - orderProduct.getQuantity();
            warehouse.setQuantity(warehouse.getQuantity() - quantityDifference);
            warehouseRepository.save(warehouse);

            orderProduct.setQuantity(orderProductDto.getQuantity());
            orderProduct.setType(orderProductDto.getType());
            orderProductRepository.save(orderProduct);

            double totalWeight = orderProductRepository.findTotalQuantityByOrderId(order.getId());
            order.setTotalWeight(totalWeight);
            orderRepository.save(order);

            logger.info("Order product with ID {} updated successfully", id);
            return new ApiResponse("Order product updated", true);

        } catch (Exception e) {
            logger.error("Error editing order product with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error updating order product", false);
        }
    }

    public ApiResponse deleteOrderProduct(Long id) {

        try {
            Optional<OrderProduct> existingOrderProduct = orderProductRepository.findById(id);

            if (existingOrderProduct.isEmpty()) {
                logger.warn("Order product not found with ID: {}", id);
                return new ApiResponse("Order product not found", false);
            }

            OrderProduct orderProduct = existingOrderProduct.get();
            Order order = orderProduct.getOrder();
            Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(orderProduct.getProduct());

            if (existingWarehouseProduct.isEmpty()) {
                logger.warn("Warehouse product not found for product: {}", orderProduct.getProduct());
                return new ApiResponse("Warehouse product not found", false);
            }

            Warehouse warehouse = existingWarehouseProduct.get();
            warehouse.setQuantity(warehouse.getQuantity() + orderProduct.getQuantity());
            warehouseRepository.save(warehouse);

            if ("kg".equals(orderProduct.getType()) || "l".equals(orderProduct.getType())) {
                order.setTotalWeight(order.getTotalWeight() - orderProduct.getQuantity());
            }

            orderProductRepository.delete(orderProduct);

            logger.info("Order product with ID {} deleted successfully", id);
            return new ApiResponse("Order product deleted", true);

        } catch (Exception e) {
            logger.error("Error deleting order product with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error deleting order product", false);
        }
    }
}

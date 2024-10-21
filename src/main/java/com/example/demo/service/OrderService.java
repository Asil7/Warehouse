package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.order.OrderDto;
import com.example.demo.entity.Company;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderProduct;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.OrderProductRepository;
import com.example.demo.repository.OrderRepository;


@Service
public class OrderService {
    
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CompanyRepository companyRepository;
    
    @Autowired
    OrderProductRepository orderProductRepository;


    public ApiResponse createOrder(OrderDto orderDto) {
        Company company = companyRepository.findById(orderDto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Order order = new Order();
        order.setUsername(orderDto.getUsername());
        order.setDelivered(false);
        order.setCompany(company);

        List<OrderProduct> productList = orderDto.getProductList().stream().map(dto -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setName(dto.getName());
            orderProduct.setQuantity(dto.getQuantity());
            orderProduct.setType(dto.getType());
            return orderProduct;
        }).collect(Collectors.toList());

        double totalWeight = calculateTotalWeight(productList);
        order.setTotalWeight(totalWeight);
        order.setProductList(productList);

        orderRepository.save(order);

        return new ApiResponse("Order created successfully", true, order);
    }

    private double calculateTotalWeight(List<OrderProduct> productList) {
        return productList.stream()
                .filter(product -> product.getType().equalsIgnoreCase("kg") || product.getType().equalsIgnoreCase("l"))
                .mapToDouble(OrderProduct::getQuantity)
                .sum();
    }
}

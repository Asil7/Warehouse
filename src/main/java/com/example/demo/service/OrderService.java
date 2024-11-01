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

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	OrderProductRepository orderProductRepository;

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse createOrder(OrderDto orderDto) {
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
	}

	public ApiResponse getAllOrders() {
		List<OrderProjection> findAllOrders = orderRepository.findAllOrders();
		return new ApiResponse("Order List", true, findAllOrders);
	}

	public ApiResponse getOrdersByUsername(String username) {
		List<OrderProjection> orders = orderRepository.findOrdersByUsername(username);
		if (orders.isEmpty()) {
			return new ApiResponse("No orders found for username: " + username, true, orders);
		}
		return new ApiResponse("Orders for username: " + username, true, orders);
	}

	public ApiResponse getOrderById(Long id) {
		OrderProjection orderById = orderRepository.findOrderById(id);
		return new ApiResponse("Order by id", true, orderById);
	}

	public ApiResponse getOrderProductsByOrderId(Long orderId) {
		List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdSorted(orderId);
		return new ApiResponse("Order Products List", true, orderProducts);
	}

	public ApiResponse deleteOrder(Long orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (!optionalOrder.isPresent()) {
			return new ApiResponse("Order not found", false);
		}

		List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdSorted(orderId);
		if (!orderProducts.isEmpty()) {

			for (OrderProduct orderProduct : orderProducts) {
				Optional<Warehouse> existingWarehouseProduct = warehouseRepository
						.findByProduct(orderProduct.getProduct());

				if (existingWarehouseProduct.isEmpty()) {
					return new ApiResponse("Product " + orderProduct.getProduct() + "not found in warehouse", false);
				}

				Warehouse warehouseProduct = existingWarehouseProduct.get();

				warehouseProduct.setQuantity(warehouseProduct.getQuantity() + orderProduct.getQuantity());
				warehouseRepository.save(warehouseProduct);

			}
			orderProductRepository.deleteAll(orderProducts);
		}

		orderRepository.deleteById(orderId);

		return new ApiResponse("Order deleted", true);
	}

}

package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.WarehouseRepository;

@Service
public class WarehouseService {

	private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

	@Autowired
	WarehouseRepository warehouseRepository;

	// public ApiResponse createProduct(@Valid WarehouseDto warehouseDto) {

	// boolean exists =
	// warehouseRepository.existsByProduct(warehouseDto.getProduct());

	// if (exists) {
	// return new ApiResponse("Product already exists", false);
	// }

	// Warehouse warehouse = new Warehouse();
	// warehouse.setProduct(warehouseDto.getProduct());
	// warehouse.setQuantity(warehouseDto.getQuantity());
	// warehouse.setType(warehouseDto.getType());

	// warehouseRepository.save(warehouse);

	// return new ApiResponse("Product created", true);
	// }

	// public ApiResponse editProduct(@Valid Long id, WarehouseDto warehouseDto) {

	// Optional<Warehouse> productOptional = warehouseRepository.findById(id);

	// if (productOptional.isPresent()) {
	// Warehouse existingProduct = productOptional.get();

	// if (warehouseRepository.existsByProductAndIdNot(warehouseDto.getProduct(),
	// id)) {
	// return new ApiResponse("Product with the name " + warehouseDto.getProduct() +
	// " already exists.",
	// false);
	// }

	// existingProduct.setProduct(warehouseDto.getProduct());
	// existingProduct.setQuantity(warehouseDto.getQuantity());
	// existingProduct.setType(warehouseDto.getType());
	// warehouseRepository.save(existingProduct);

	// return new ApiResponse("Product updated", true);
	// } else {
	// return new ApiResponse("Product not found", false);
	// }
	// }

	public ApiResponse getAllProducts() {
		try {
			List<Warehouse> productList = warehouseRepository.findAllByOrderByCreatedAtDesc();
			logger.info("Get all products from warehouse {} ", productList.size());
			return new ApiResponse("Product List", true, productList);
		} catch (Exception e) {
			logger.error("Error fetching all warehouse products: {}", e.getMessage(), e);
			return new ApiResponse("Error fetching warehouse products", false);
		}

	}

	public ApiResponse deleteProduct(Long id) {
		try {
			Optional<Warehouse> productOptional = warehouseRepository.findById(id);
			if (productOptional.isPresent()) {
				Warehouse product = productOptional.get();

				warehouseRepository.delete(product);
				logger.info("Warehouse Product with ID {} deleted successfully", id);
				return new ApiResponse("Product deleted", true);
			} else {
				logger.warn("Warehouse Product with ID {} not found for deletion", id);
				return new ApiResponse("Product not found", false);
			}
		} catch (Exception e) {
			logger.error("Error deleting Warehouse product with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error deleting product", false);
		}
	}
}

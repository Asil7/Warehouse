package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.entity.StoreHistory;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.StoreHistoryRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class StoreHistoryService {

	private static final Logger logger = LoggerFactory.getLogger(StoreHistoryService.class);

	@Autowired
	StoreHistoryRepository storeRepository;

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse createStoreProduct(StoreDto storeDto) {
		try {
			Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(storeDto.getProduct());

			if (existingWarehouseProduct.isPresent()) {
				Warehouse warehouse = existingWarehouseProduct.get();
				warehouse.setQuantity(warehouse.getQuantity() + storeDto.getQuantity());
				warehouse.setPrice(storeDto.getPrice());
				warehouseRepository.save(warehouse);
				logger.info("Updated warehouse {} product quantity to: {}", warehouse.getProduct(),
						warehouse.getQuantity());
			} else {
				Warehouse newWarehouseProduct = new Warehouse(storeDto.getProduct(), storeDto.getQuantity(),
						storeDto.getType(), storeDto.getPrice());
				warehouseRepository.save(newWarehouseProduct);
				logger.info("New warehouse product saved with ID: {}", newWarehouseProduct.getId());
			}

			StoreHistory newStore = new StoreHistory(storeDto.getProduct(), storeDto.getQuantity(), storeDto.getPrice(),
					storeDto.getType(), storeDto.isReceived(), storeDto.isPaid());
			storeRepository.save(newStore);
			logger.info("Store history record saved with ID: {}", newStore.getId());

			return new ApiResponse("Product added", true);
		} catch (Exception e) {
			logger.error("Error creating store product: {}", e.getMessage(), e);
			return new ApiResponse("Error adding product", false);
		}
	}

	public ApiResponse updateStoreProduct(Long id, StoreDto storeDto) {
		try {
			Optional<StoreHistory> existingStoreProduct = storeRepository.findById(id);

			if (!existingStoreProduct.isPresent()) {
				logger.warn("Store product not found with ID: {}", id);
				return new ApiResponse("Store Product not found", false);
			}

			StoreHistory store = existingStoreProduct.get();
			double quantityDifference = storeDto.getQuantity() - store.getQuantity();

			store.setProduct(storeDto.getProduct());
			store.setQuantity(storeDto.getQuantity());
			store.setPrice(storeDto.getPrice());
			store.setType(storeDto.getType());
			storeRepository.save(store);
			logger.info("Updated store product with ID: {}", store.getId());

			Optional<Warehouse> existingWarehouseProductOpt = warehouseRepository.findByProduct(storeDto.getProduct());

			if (existingWarehouseProductOpt.isPresent()) {
				Warehouse warehouse = existingWarehouseProductOpt.get();
				warehouse.setQuantity(warehouse.getQuantity() + quantityDifference);
				warehouse.setPrice(store.getPrice());
				warehouseRepository.save(warehouse);
				logger.info("Updated warehouse product quantity to: {}", warehouse.getQuantity());
			}

			return new ApiResponse("Store Product updated", true);
		} catch (Exception e) {
			logger.error("Error updating store product with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating store product", false);
		}
	}

	public ApiResponse deleteStoreProduct(Long id) {
		try {
			Optional<StoreHistory> storeProductById = storeRepository.findById(id);

			if (storeProductById.isEmpty()) {
				logger.warn("Store product not found with ID: {}", id);
				return new ApiResponse("Product not found", false);
			}

			StoreHistory store = storeProductById.get();
			Optional<Warehouse> warehouseProduct = warehouseRepository.findByProduct(store.getProduct());

			if (warehouseProduct.isEmpty()) {
				logger.warn("Warehouse product not found for store product with ID: {}", store.getId());
				return new ApiResponse("Product not found", false);
			}

			Warehouse warehouse = warehouseProduct.get();
			warehouse.setQuantity(warehouse.getQuantity() - store.getQuantity());
			warehouseRepository.save(warehouse);
			logger.info("Updated warehouse product quantity after deletion, new quantity: {}", warehouse.getQuantity());

			storeRepository.delete(store);
			logger.info("Deleted store product with ID: {}", store.getId());

			return new ApiResponse("Store product deleted", true);
		} catch (Exception e) {
			logger.error("Error deleting store product with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error deleting store product", false);
		}
	}

	public ApiResponse getAllStoreProducts() {
		try {
			List<StoreHistory> storeProductsList = storeRepository.findAllByCreatedAtDesc();
			logger.info("Get {} store products", storeProductsList.size());
			return new ApiResponse("Store Products List", true, storeProductsList);
		} catch (Exception e) {
			logger.error("Error fetching store products list: {}", e.getMessage(), e);
			return new ApiResponse("Error fetching store products list", false);
		}
	}

	public ApiResponse updateStoreHistoryPaidStatus(Long id, boolean paid) {
		try {
			Optional<StoreHistory> optionalStoreHistory = storeRepository.findById(id);
			if (optionalStoreHistory.isEmpty()) {
				logger.warn("Store product not found with ID: {}", id);
				return new ApiResponse("Store product not found", false);
			}

			StoreHistory storeHistory = optionalStoreHistory.get();
			storeHistory.setPaid(paid);
			storeRepository.save(storeHistory);
			logger.info("Updated paid status for store product with ID: {} to {}", storeHistory.getId(), paid);

			return new ApiResponse("Paid status updated", true, storeHistory);
		} catch (Exception e) {
			logger.error("Error updating paid status for store product with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating paid status", false);
		}
	}
}

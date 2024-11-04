package com.example.demo.service;

import java.util.List;
import java.util.Optional;

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

	@Autowired
	StoreHistoryRepository storeRepository;

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse createStoreProduct(StoreDto storeDto) {
		Optional<Warehouse> existingWarehouseProduct = warehouseRepository.findByProduct(storeDto.getProduct());

		if (existingWarehouseProduct.isPresent()) {
			Warehouse warehouse = existingWarehouseProduct.get();
			warehouse.setQuantity(warehouse.getQuantity() + storeDto.getQuantity());
			warehouseRepository.save(warehouse);
		} else {
			Warehouse newWarehouseProduct = new Warehouse(storeDto.getProduct(), storeDto.getQuantity(),
					storeDto.getType());
			warehouseRepository.save(newWarehouseProduct);
		}

		StoreHistory newStore = new StoreHistory(storeDto.getProduct(), storeDto.getQuantity(), storeDto.getPrice(),
				storeDto.getType(), storeDto.isReceived(), storeDto.isPaid());
		storeRepository.save(newStore);

		return new ApiResponse("Product added", true);
	}

	public ApiResponse updateStoreProduct(Long id, StoreDto storeDto) {
		Optional<StoreHistory> existingStoreProduct = storeRepository.findById(id);

		if (!existingStoreProduct.isPresent()) {
			return new ApiResponse("Store Product not found", false);
		}

		StoreHistory store = existingStoreProduct.get();

		Long quantityDifference = storeDto.getQuantity() - store.getQuantity();

		store.setProduct(storeDto.getProduct());
		store.setQuantity(storeDto.getQuantity());
		store.setPrice(storeDto.getPrice());
		store.setType(storeDto.getType());
		storeRepository.save(store);

		Optional<Warehouse> existingWarehouseProductOpt = warehouseRepository.findByProduct(storeDto.getProduct());

		if (existingWarehouseProductOpt.isPresent()) {
			Warehouse warehouse = existingWarehouseProductOpt.get();
			warehouse.setQuantity(warehouse.getQuantity() + quantityDifference);
			warehouseRepository.save(warehouse);
		}

		return new ApiResponse("Store Product updated", true);
	}

	public ApiResponse deleteStoreProduct(Long id) {
		Optional<StoreHistory> storeProductById = storeRepository.findById(id);

		if (storeProductById.isEmpty()) {
			return new ApiResponse("Product not found", false);
		}

		StoreHistory store = storeProductById.get();

		Optional<Warehouse> warehouseProduct = warehouseRepository.findByProduct(store.getProduct());

		if (warehouseProduct.isEmpty()) {
			return new ApiResponse("Product not found", false);
		}

		Warehouse warehouse = warehouseProduct.get();

		warehouse.setQuantity(warehouse.getQuantity() - store.getQuantity());

		warehouseRepository.save(warehouse);

		storeRepository.delete(store);

		return new ApiResponse("Store product deleted", true);
	}

	public ApiResponse getAllStoreProducts() {
		List<StoreHistory> storeProductsList = storeRepository.findAll();
		return new ApiResponse("Store Products List", true, storeProductsList);
	}
}

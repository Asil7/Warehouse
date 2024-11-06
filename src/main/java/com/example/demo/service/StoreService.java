package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.store.StoreDto;
import com.example.demo.entity.Store;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class StoreService {

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	StoreHistoryService storeHistoryService;

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse addProduct(StoreDto storeDto) {

		if (storeRepository.existsByProduct(storeDto.getProduct())) {
			return new ApiResponse("Product with the name " + storeDto.getProduct() + " already exists.", false);
		}

		Store store = new Store();

		store.setProduct(storeDto.getProduct());
		store.setQuantity(storeDto.getQuantity());
		store.setPrice(storeDto.getPrice());
		store.setType(storeDto.getType());
		store.setReceived(false);
		store.setPaid(false);

		storeRepository.save(store);

		return new ApiResponse("Product added", true);
	}

	public ApiResponse updateProduct(Long id, StoreDto storeDto) {

		Optional<Store> optionalStore = storeRepository.findById(id);

		if (optionalStore.isEmpty()) {
			return new ApiResponse("Store product not found", false);
		}

		Store store = optionalStore.get();

		store.setProduct(storeDto.getProduct());
		store.setQuantity(storeDto.getQuantity());
		store.setPrice(storeDto.getPrice());
		store.setType(storeDto.getType());

		storeRepository.save(store);

		return new ApiResponse("Product updated", true);
	}

	public ApiResponse getStoreProductList() {
		List<Store> storeProductList = storeRepository.getStoreProductList();
		return new ApiResponse("Store Product List", true, storeProductList);
	}

	public ApiResponse updateStorePaidStatus(Long id, boolean paid) {
		Optional<Store> optionalStore = storeRepository.findById(id);
		if (optionalStore.isEmpty()) {
			return new ApiResponse("Store product not found", false);
		}

		Store store = optionalStore.get();
		store.setPaid(paid);
		storeRepository.save(store);

		return new ApiResponse("Paid", true, store);
	}

	public ApiResponse updateStoreReceivedStatus(Long id, StoreDto storeDto) {
		Optional<Store> optionalStore = storeRepository.findById(id);
		if (optionalStore.isEmpty()) {
			return new ApiResponse("Store product not found", false);
		}
		Store store = optionalStore.get();
		store.setReceived(storeDto.isReceived());
		storeRepository.save(store);

		ApiResponse createStoreProduct = storeHistoryService.createStoreProduct(storeDto);

		if (!createStoreProduct.isSuccess()) {
			return new ApiResponse("Store received status updated, but failed to create store history", false);
		}

		storeRepository.delete(store);

		return new ApiResponse("Product received", true, store);
	}

	public ApiResponse addProductFromWarehouse() {
		 List<Warehouse> warehouses = warehouseRepository.findAll();

		    for (Warehouse warehouse : warehouses) {
		        Optional<Store> existingStoreOpt = storeRepository.findByProduct(warehouse.getProduct());

		        if (warehouse.getQuantity() >= 0) {
		            existingStoreOpt.ifPresent(storeRepository::delete);
		        } else {
		            long positiveQuantity = Math.abs(warehouse.getQuantity());

		            if (existingStoreOpt.isPresent()) {
		                Store existingStore = existingStoreOpt.get();
		                existingStore.setQuantity(positiveQuantity);
		                existingStore.setType(warehouse.getType()); 
		                storeRepository.save(existingStore);
		            } else {
		                Store newStore = new Store();
		                newStore.setProduct(warehouse.getProduct());
		                newStore.setQuantity(positiveQuantity);
		                newStore.setType(warehouse.getType());
		                newStore.setReceived(false);
		                newStore.setPaid(false);

		                storeRepository.save(newStore);
		            }
		        }
		    }
		return new ApiResponse("Success", true);
	}

}

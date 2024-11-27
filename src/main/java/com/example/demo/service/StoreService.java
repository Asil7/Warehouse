package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	StoreHistoryService storeHistoryService;

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse addProduct(StoreDto storeDto) {
		try {
			if (storeRepository.existsByProduct(storeDto.getProduct())) {
				logger.warn("Product with name '{}' already exists.", storeDto.getProduct());
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

			logger.info("Product '{}' added successfully.", storeDto.getProduct());
			return new ApiResponse("Product added", true);
		} catch (Exception e) {
			logger.error("Error adding product: ", e);
			return new ApiResponse("Failed to add product", false);
		}
	}

	public ApiResponse updateProduct(Long id, StoreDto storeDto) {
		try {
			Optional<Store> optionalStore = storeRepository.findById(id);
			if (optionalStore.isEmpty()) {
				logger.warn("Store product with ID '{}' not found.", id);
				return new ApiResponse("Store product not found", false);
			}

			Store store = optionalStore.get();
			store.setProduct(storeDto.getProduct());
			store.setQuantity(storeDto.getQuantity());
			store.setPrice(storeDto.getPrice());
			store.setType(storeDto.getType());

			storeRepository.save(store);
			logger.info("Product '{}' updated successfully.", id);
			return new ApiResponse("Product updated", true);
		} catch (Exception e) {
			logger.error("Error updating product with ID '{}': ", id, e);
			return new ApiResponse("Failed to update product", false);
		}
	}

	public ApiResponse getStoreProductList() {
		try {
			List<Store> storeProductList = storeRepository.getStoreProductList();
			logger.info("Fetched store product list.");
			return new ApiResponse("Store Product List", true, storeProductList);
		} catch (Exception e) {
			logger.error("Error fetching store product list: ", e);
			return new ApiResponse("Failed to fetch product list", false);
		}
	}

	public ApiResponse updateStorePaidStatus(Long id, boolean paid) {
		try {
			Optional<Store> optionalStore = storeRepository.findById(id);
			if (optionalStore.isEmpty()) {
				logger.warn("Store product with ID '{}' not found.", id);
				return new ApiResponse("Store product not found", false);
			}

			Store store = optionalStore.get();
			store.setPaid(paid);
			storeRepository.save(store);

			logger.info("Updated paid status for product with ID '{}'.", id);
			return new ApiResponse("Paid", true, store);
		} catch (Exception e) {
			logger.error("Error updating paid status for product with ID '{}': ", id, e);
			return new ApiResponse("Failed to update paid status", false);
		}
	}

	public ApiResponse updateStoreReceivedStatus(Long id, StoreDto storeDto) {
		try {
			Optional<Store> optionalStore = storeRepository.findById(id);
			if (optionalStore.isEmpty()) {
				logger.warn("Store product with ID '{}' not found.", id);
				return new ApiResponse("Store product not found", false);
			}

			Store store = optionalStore.get();
			store.setReceived(storeDto.isReceived());
			storeRepository.save(store);

			ApiResponse createStoreProduct = storeHistoryService.createStoreProduct(storeDto);

			if (!createStoreProduct.isSuccess()) {
				logger.warn("Store received status updated but failed to create store history for product ID '{}'.",
						id);
				return new ApiResponse("Store received status updated, but failed to create store history", false);
			}

			storeRepository.delete(store);
			logger.info("Product with ID '{}' marked as received and deleted from store.", id);
			return new ApiResponse("Product received", true, store);
		} catch (Exception e) {
			logger.error("Error updating received status for product with ID '{}': ", id, e);
			return new ApiResponse("Failed to update received status", false);
		}
	}

	public ApiResponse addProductFromWarehouse() {
		try {
			List<Warehouse> warehouses = warehouseRepository.findAll();
			for (Warehouse warehouse : warehouses) {
				Optional<Store> existingStoreOpt = storeRepository.findByProduct(warehouse.getProduct());

				if (warehouse.getQuantity() >= 0) {
					existingStoreOpt.ifPresent(storeRepository::delete);
				} else {
					double positiveQuantity = Math.abs(warehouse.getQuantity());

					if (existingStoreOpt.isPresent()) {
						Store existingStore = existingStoreOpt.get();
						existingStore.setQuantity(positiveQuantity);
						existingStore.setType(warehouse.getType());
						existingStore.setPrice(warehouse.getPrice());
						storeRepository.save(existingStore);
					} else {
						Store newStore = new Store();
						newStore.setProduct(warehouse.getProduct());
						newStore.setQuantity(positiveQuantity);
						newStore.setType(warehouse.getType());
						newStore.setPrice(warehouse.getPrice());
						newStore.setReceived(false);
						newStore.setPaid(false);

						storeRepository.save(newStore);
					}
				}
			}
			logger.info("Warehouse products successfully added to store.");
			return new ApiResponse("Success", true);
		} catch (Exception e) {
			logger.error("Error adding products from warehouse: ", e);
			return new ApiResponse("Failed to add products from warehouse", false);
		}
	}

	public ApiResponse deleteProduct(Long id) {
		try {
			Optional<Store> findById = storeRepository.findById(id);
			if (findById.isEmpty()) {
				logger.warn("Product with ID '{}' not found.", id);
				return new ApiResponse("Product not found", false);
			}

			Store store = findById.get();
			storeRepository.delete(store);

			logger.info("Product with ID '{}' deleted successfully.", id);
			return new ApiResponse("Product deleted", true);
		} catch (Exception e) {
			logger.error("Error deleting product with ID '{}': ", id, e);
			return new ApiResponse("Failed to delete product", false);
		}
	}
}

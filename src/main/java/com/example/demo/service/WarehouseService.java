package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.warehouse.WarehouseDto;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.WarehouseRepository;

import jakarta.validation.Valid;

@Service
public class WarehouseService {

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse createProduct(@Valid WarehouseDto warehouseDto) {

		boolean exists = warehouseRepository.existsByProduct(warehouseDto.getProduct());

		if (exists) {
			return new ApiResponse("Product already exists", false);
		}

		Warehouse warehouse = new Warehouse();
		warehouse.setProduct(warehouseDto.getProduct());
		warehouse.setQuantity(warehouseDto.getQuantity());
		warehouse.setType(warehouseDto.getType());

		warehouseRepository.save(warehouse);

		return new ApiResponse("Product created", true);
	}

	public ApiResponse editProduct(@Valid Long id, WarehouseDto warehouseDto) {

		Optional<Warehouse> productOptional = warehouseRepository.findById(id);

		if (productOptional.isPresent()) {
			Warehouse existingProduct = productOptional.get();

			if (warehouseRepository.existsByProductAndIdNot(warehouseDto.getProduct(), id)) {
				return new ApiResponse("Product with the name " + warehouseDto.getProduct() + " already exists.",
						false);
			}

			existingProduct.setProduct(warehouseDto.getProduct());
			existingProduct.setQuantity(warehouseDto.getQuantity());
			existingProduct.setType(warehouseDto.getType());
			warehouseRepository.save(existingProduct);

			return new ApiResponse("Product updated", true);
		} else {
			return new ApiResponse("Product not found", false);
		}
	}
	
	public ApiResponse getAllProducts() {
		List<Warehouse> productList = warehouseRepository.findAll();
		return new ApiResponse("Product List", true, productList);
	}

	public ApiResponse deleteProduct(Long id) {
		Optional<Warehouse> productOptional = warehouseRepository.findById(id);
		if (productOptional.isPresent()) {
			Warehouse product = productOptional.get();

			warehouseRepository.delete(product);
			return new ApiResponse("Product deleted", true);
		} else {
			return new ApiResponse("Product not found", false);
		}
	}
	
	public ApiResponse addQauntity(Long id, Long quantity) {
		Optional<Warehouse> productOptional = warehouseRepository.findById(id);
		if (productOptional.isPresent()) {
			Warehouse existingProduct = productOptional.get();
			long updatedQuantity = existingProduct.getQuantity() + quantity;
			existingProduct.setQuantity(updatedQuantity);
			warehouseRepository.save(existingProduct);
			return new ApiResponse("Quantity updated", true);
		} else {
			return new ApiResponse("Product not found", false);
		}
	}

	public ApiResponse subtractQuantity(Long id, Long quantity) {
		Optional<Warehouse> productOptional = warehouseRepository.findById(id);
		if (productOptional.isPresent()) {
			Warehouse existingProduct = productOptional.get();
			long updatedQuantity = existingProduct.getQuantity() - quantity;
			existingProduct.setQuantity(updatedQuantity);
			warehouseRepository.save(existingProduct);
			return new ApiResponse("Quantity subtracted", true);
		} else {
			return new ApiResponse("Product not found", false);
		}
	}
}


























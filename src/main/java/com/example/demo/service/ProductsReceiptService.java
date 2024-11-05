package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.product.ProductsReceiptDto;
import com.example.demo.entity.ProductsReceipt;
import com.example.demo.entity.Warehouse;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.ProductsReceiptRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class ProductsReceiptService {

	@Autowired
	ProductsReceiptRepository productsReceiptRepository;

	@Autowired
	WarehouseRepository warehouseRepository;

	public ApiResponse createProductsReceipt(ProductsReceiptDto productsReceiptDto) {
		Optional<Warehouse> existingWarehouseProduct = warehouseRepository
				.findByProduct(productsReceiptDto.getProduct());

		if (existingWarehouseProduct.isPresent()) {
			Warehouse warehouse = existingWarehouseProduct.get();
			warehouse.setQuantity(warehouse.getQuantity() + productsReceiptDto.getQuantity());
			warehouseRepository.save(warehouse);
		} else {
			Warehouse newWarehouseProduct = new Warehouse(productsReceiptDto.getProduct(),
					productsReceiptDto.getQuantity(), productsReceiptDto.getType());
			warehouseRepository.save(newWarehouseProduct);
		}

		ProductsReceipt newProductsReceipt = new ProductsReceipt(productsReceiptDto.getProduct(),
				productsReceiptDto.getQuantity(), productsReceiptDto.getType());
		productsReceiptRepository.save(newProductsReceipt);

		return new ApiResponse("Product added", true);
	}

	public ApiResponse updateReceivedProduct(Long id, ProductsReceiptDto productsReceiptDto) {
		Optional<ProductsReceipt> existingProductsReceiptOpt = productsReceiptRepository.findById(id);

		if (!existingProductsReceiptOpt.isPresent()) {
			return new ApiResponse("ProductsReceipt not found", false);
		}

		ProductsReceipt existingProductsReceipt = existingProductsReceiptOpt.get();

		Long quantityDifference = productsReceiptDto.getQuantity() - existingProductsReceipt.getQuantity();

		existingProductsReceipt.setProduct(productsReceiptDto.getProduct());
		existingProductsReceipt.setQuantity(productsReceiptDto.getQuantity());
		existingProductsReceipt.setType(productsReceiptDto.getType());
		productsReceiptRepository.save(existingProductsReceipt);

		Optional<Warehouse> existingWarehouseProductOpt = warehouseRepository
				.findByProduct(productsReceiptDto.getProduct());

		if (existingWarehouseProductOpt.isPresent()) {
			Warehouse warehouse = existingWarehouseProductOpt.get();
			warehouse.setQuantity(warehouse.getQuantity() + quantityDifference);
			warehouseRepository.save(warehouse);
		}

		return new ApiResponse("Products Receipt updated", true);
	}

	public ApiResponse deleteProduct(Long id) {
		Optional<ProductsReceipt> productById = productsReceiptRepository.findById(id);

		if (productById.isEmpty()) {
			return new ApiResponse("Product not found", false);
		}

		ProductsReceipt productsReceipt = productById.get();

		Optional<Warehouse> warehouseProduct = warehouseRepository.findByProduct(productsReceipt.getProduct());

		if (warehouseProduct.isEmpty()) {
			return new ApiResponse("Product not found", false);
		}

		Warehouse warehouse = warehouseProduct.get();

		warehouse.setQuantity(warehouse.getQuantity() - productsReceipt.getQuantity());

		warehouseRepository.save(warehouse);

		productsReceiptRepository.delete(productsReceipt);

		return new ApiResponse("Product deleted", true);
	}

	public ApiResponse getAllReceivedProducts() {
		List<ProductsReceipt> receivedProductsList = productsReceiptRepository.findAllByCreatedAtDesc();
		return new ApiResponse("Received Products List", true, receivedProductsList);
	}

}

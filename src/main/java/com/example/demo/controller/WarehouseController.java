package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payload.ApiResponse;
import com.example.demo.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

	private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

	@Autowired
	WarehouseService warehouseService;

	// @PreAuthorize("hasAuthority('ADD_WAREHOUSE_PRODUCT')")
	// @PostMapping
	// public HttpEntity<?> createProduct(@Valid @RequestBody WarehouseDto
	// warehouseDto) {
	// ApiResponse apiResponse = warehouseService.createProduct(warehouseDto);
	// return ResponseEntity.status(apiResponse.isSuccess() ? 200 :
	// 409).body(apiResponse);
	// }

	// // @PreAuthorize("hasAuthority('EDIT_WAREHOUSE_PRODUCT')")
	// @PutMapping("/{id}")
	// public HttpEntity<?> editProduct(@Valid @PathVariable Long id, @RequestBody
	// WarehouseDto warehouseDto) {
	// ApiResponse apiResponse = warehouseService.editProduct(id, warehouseDto);
	// return ResponseEntity.status(apiResponse.isSuccess() ? 200 :
	// 409).body(apiResponse);
	// }

	// @PreAuthorize("hasAuthority('VIEW_WAREHOUSE_PRODUCT_LIST')")
	@GetMapping
	public HttpEntity<?> getProduct() {
		try {
			ApiResponse apiResponse = warehouseService.getAllProducts();
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching warehouse product list: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error fetching warehouse product list", false));
		}

	}

	// @PreAuthorize("hasAuthority('DELETE_WAREHOUSE_PRODUCT')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteProduct(@PathVariable Long id) {
		try {
			ApiResponse apiResponse = warehouseService.deleteProduct(id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error deleting warehouse product with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error deleting warehouse product", false));
		}
	}
}

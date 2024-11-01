package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.warehouse.WarehouseDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.WarehouseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

	@Autowired
	WarehouseService warehouseService;

	@PreAuthorize("hasAuthority('ADD_WAREHOUSE_PRODUCT')")
	@PostMapping
	public HttpEntity<?> createProduct(@Valid @RequestBody WarehouseDto warehouseDto) {
		ApiResponse apiResponse = warehouseService.createProduct(warehouseDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('EDIT_WAREHOUSE_PRODUCT')")
	@PutMapping("/{id}")
	public HttpEntity<?> editProduct(@Valid @PathVariable Long id, @RequestBody WarehouseDto warehouseDto) {
		ApiResponse apiResponse = warehouseService.editProduct(id, warehouseDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('VIEW_WAREHOUSE_PRODUCT_LIST')")
	@GetMapping
	public HttpEntity<?> getProduct() {
		ApiResponse apiResponse = warehouseService.getAllProducts();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('DELETE_WAREHOUSE_PRODUCT')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteProduct(@PathVariable Long id) {
		ApiResponse apiResponse = warehouseService.deleteProduct(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
//	@PreAuthorize("hasAuthority('ADD_QUANTITY')")
	@PutMapping("/addQuantity/{id}/{quantity}")
	public HttpEntity<?> addQuantity(@Valid @PathVariable Long id, @PathVariable Long quantity) {
		ApiResponse apiResponse = warehouseService.addQauntity(id, quantity);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('SUBTRACT_QUANTITY')")
	@PutMapping("/subtractQuantity/{id}/{quantity}")
	public HttpEntity<?> subtractQuantity(@Valid @PathVariable Long id, @PathVariable Long quantity) {
		ApiResponse apiResponse = warehouseService.subtractQuantity(id, quantity);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

}









































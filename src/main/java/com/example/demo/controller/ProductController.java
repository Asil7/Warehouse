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

import com.example.demo.dto.product.ProductDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	@Autowired
	ProductService productService;

	@PreAuthorize("hasAuthority('ADD_PRODUCT')")
	@PostMapping
	public HttpEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
		ApiResponse apiResponse = productService.createProduct(productDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('EDIT_PRODUCT')")
	@PutMapping("/{id}")
	public HttpEntity<?> editProduct(@Valid @PathVariable Long id, @RequestBody ProductDto productDto) {
		ApiResponse apiResponse = productService.editProduct(id, productDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('VIEW_PRODUCT_LIST')")
	@GetMapping
	public HttpEntity<?> getProduct() {
		ApiResponse apiResponse = productService.getAllProducts();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('DELETE_PRODUCT')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteProduct(@PathVariable Long id) {
		ApiResponse apiResponse = productService.deleteProduct(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
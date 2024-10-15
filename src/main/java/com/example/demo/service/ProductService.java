package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.product.ProductDto;
import com.example.demo.entity.Product;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.ProductRepository;

import jakarta.validation.Valid;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	public ApiResponse createProduct(@Valid ProductDto productDto) {

		if (productRepository.existsByName(productDto.getName())) {
			return new ApiResponse("Product with the name " + productDto.getName() + " already exists.", false);
		}
		
		Product product = new Product();
		
		product.setName(productDto.getName());
		product.setType(productDto.getType());
		productRepository.save(product);
		return new ApiResponse("Product created", true);
	}

	public ApiResponse editProduct(@Valid Long id, ProductDto productDto) {

	    Optional<Product> productOptional = productRepository.findById(id);

	    if (productOptional.isPresent()) {
	        Product existingProduct = productOptional.get();

	        if (productRepository.existsByNameAndIdNot(productDto.getName(), id)) {
	            return new ApiResponse("Product with the name " + productDto.getName() + " already exists.", false);
	        }

	        existingProduct.setName(productDto.getName());
	        existingProduct.setType(productDto.getType());
	        productRepository.save(existingProduct);

	        return new ApiResponse("Product updated", true);
	    } else {
	        return new ApiResponse("Product not found", false);
	    }
	}


	public ApiResponse getAllProducts() {
		List<Product> productList = productRepository.findAll();
		return new ApiResponse("Product List", true, productList);
	}

	public ApiResponse deleteProduct(Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		if (productOptional.isPresent()) {
			Product product = productOptional.get();

			productRepository.delete(product);
			return new ApiResponse("Product deleted", true);
		} else {
			return new ApiResponse("Product not found", false);
		}
	}
}

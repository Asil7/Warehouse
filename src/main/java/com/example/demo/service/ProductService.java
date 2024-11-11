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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    public ApiResponse createProduct(@Valid ProductDto productDto) {

        try {
            if (productRepository.existsByName(productDto.getName())) {
                logger.warn("Product with the name {} already exists", productDto.getName());
                return new ApiResponse("Product with the name " + productDto.getName() + " already exists.", false);
            }

            Product product = new Product();
            product.setName(productDto.getName());
            product.setType(productDto.getType());
            productRepository.save(product);

            logger.info("Product created with name: {}", productDto.getName());
            return new ApiResponse("Product created", true);

        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            return new ApiResponse("Error creating product", false);
        }
    }

    public ApiResponse editProduct(@Valid Long id, ProductDto productDto) {

        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isEmpty()) {
                logger.warn("Product not found with ID: {}", id);
                return new ApiResponse("Product not found", false);
            }

            Product existingProduct = productOptional.get();

            if (productRepository.existsByNameAndIdNot(productDto.getName(), id)) {
                logger.warn("Product with the name {} already exists for a different ID", productDto.getName());
                return new ApiResponse("Product with the name " + productDto.getName() + " already exists.", false);
            }

            existingProduct.setName(productDto.getName());
            existingProduct.setType(productDto.getType());
            productRepository.save(existingProduct);

            logger.info("Product updated with ID: {}", id);
            return new ApiResponse("Product updated", true);

        } catch (Exception e) {
            logger.error("Error editing product with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error editing product", false);
        }
    }

    public ApiResponse getAllProducts() {

        try {
            List<Product> productList = productRepository.findAll();
            logger.info("Fetched {} products", productList.size());
            return new ApiResponse("Product List", true, productList);

        } catch (Exception e) {
            logger.error("Error fetching products: {}", e.getMessage(), e);
            return new ApiResponse("Error fetching products", false);
        }
    }

    public ApiResponse deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);

        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isEmpty()) {
                logger.warn("Product not found with ID: {}", id);
                return new ApiResponse("Product not found", false);
            }

            Product product = productOptional.get();
            productRepository.delete(product);

            logger.info("Product deleted with ID: {}", id);
            return new ApiResponse("Product deleted", true);

        } catch (Exception e) {
            logger.error("Error deleting product with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error deleting product", false);
        }
    }
}

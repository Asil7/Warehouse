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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductsReceiptService {

    private static final Logger logger = LoggerFactory.getLogger(ProductsReceiptService.class);

    @Autowired
    ProductsReceiptRepository productsReceiptRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public ApiResponse createProductsReceipt(ProductsReceiptDto productsReceiptDto) {

        try {
            Optional<Warehouse> existingWarehouseProduct = warehouseRepository
                    .findByProduct(productsReceiptDto.getProduct());

            if (existingWarehouseProduct.isPresent()) {

                Warehouse warehouse = existingWarehouseProduct.get();
                warehouse.setQuantity(warehouse.getQuantity() + productsReceiptDto.getQuantity());
                warehouse.setPrice(productsReceiptDto.getPrice());
                warehouseRepository.save(warehouse);

                logger.info("Warehouse quantity updated for product: {} to {}", productsReceiptDto.getProduct(),
                        warehouse.getQuantity());
            } else {

                Warehouse newWarehouseProduct = new Warehouse(productsReceiptDto.getProduct(),
                        productsReceiptDto.getQuantity(), productsReceiptDto.getType(), productsReceiptDto.getPrice());
                warehouseRepository.save(newWarehouseProduct);

                logger.info("New warehouse entry created for product: {}", productsReceiptDto.getProduct());
            }

            ProductsReceipt newProductsReceipt = new ProductsReceipt(productsReceiptDto.getProduct(),
                    productsReceiptDto.getQuantity(), productsReceiptDto.getPrice(), productsReceiptDto.getType());
            productsReceiptRepository.save(newProductsReceipt);

            logger.info("New ProductsReceipt saved for product: {}", productsReceiptDto.getProduct());
            return new ApiResponse("Product added", true);

        } catch (Exception e) {
            logger.error("Error creating product receipt for product: {}: {}", productsReceiptDto.getProduct(),
                    e.getMessage(), e);
            return new ApiResponse("Error creating product receipt", false);
        }
    }

    public ApiResponse updateReceivedProduct(Long id, ProductsReceiptDto productsReceiptDto) {

        try {
            Optional<ProductsReceipt> existingProductsReceiptOpt = productsReceiptRepository.findById(id);

            if (!existingProductsReceiptOpt.isPresent()) {
                logger.warn("ProductsReceipt not found with ID: {}", id);
                return new ApiResponse("ProductsReceipt not found", false);
            }

            ProductsReceipt existingProductsReceipt = existingProductsReceiptOpt.get();
            double quantityDifference = productsReceiptDto.getQuantity() - existingProductsReceipt.getQuantity();

            existingProductsReceipt.setProduct(productsReceiptDto.getProduct());
            existingProductsReceipt.setQuantity(productsReceiptDto.getQuantity());
            existingProductsReceipt.setType(productsReceiptDto.getType());
            productsReceiptRepository.save(existingProductsReceipt);

            logger.info("ProductsReceipt updated for product: {}", productsReceiptDto.getProduct());

            Optional<Warehouse> existingWarehouseProductOpt = warehouseRepository
                    .findByProduct(productsReceiptDto.getProduct());

            if (existingWarehouseProductOpt.isPresent()) {
                Warehouse warehouse = existingWarehouseProductOpt.get();
                logger.info("Updating warehouse quantity for product: {}", productsReceiptDto.getProduct());

                warehouse.setQuantity(warehouse.getQuantity() + quantityDifference);
                warehouseRepository.save(warehouse);

                logger.info("Warehouse quantity updated for product: {} to {}", productsReceiptDto.getProduct(),
                        warehouse.getQuantity());
            } else {
                logger.warn("Warehouse entry not found for product: {}", productsReceiptDto.getProduct());
            }

            logger.info("Successfully updated ProductsReceipt with ID: {}", id);
            return new ApiResponse("Products Receipt updated", true);

        } catch (Exception e) {
            logger.error("Error updating product receipt with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error updating product receipt", false);
        }
    }

    public ApiResponse deleteProduct(Long id) {

        try {
            Optional<ProductsReceipt> productById = productsReceiptRepository.findById(id);

            if (productById.isEmpty()) {
                logger.warn("Product not found with ID: {}", id);
                return new ApiResponse("Product not found", false);
            }

            ProductsReceipt productsReceipt = productById.get();
            Optional<Warehouse> warehouseProduct = warehouseRepository.findByProduct(productsReceipt.getProduct());

            if (warehouseProduct.isEmpty()) {
                logger.warn("Warehouse product not found for product: {}", productsReceipt.getProduct());
                return new ApiResponse("Product not found", false);
            }

            Warehouse warehouse = warehouseProduct.get();

            warehouse.setQuantity(warehouse.getQuantity() - productsReceipt.getQuantity());
            warehouseRepository.save(warehouse);

            productsReceiptRepository.delete(productsReceipt);

            logger.info("Product and ProductsReceipt with ID: {} successfully deleted", id);
            return new ApiResponse("Product deleted", true);

        } catch (Exception e) {
            logger.error("Error deleting product with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error deleting product", false);
        }
    }

    public ApiResponse getAllReceivedProducts() {

        try {
            List<ProductsReceipt> receivedProductsList = productsReceiptRepository.findAllByCreatedAtDesc();
            logger.info("Successfully retrieved {} received products", receivedProductsList.size());
            return new ApiResponse("Received Products List", true, receivedProductsList);
        } catch (Exception e) {
            logger.error("Error retrieving all received products: {}", e.getMessage(), e);
            return new ApiResponse("Error retrieving received products", false);
        }
    }
}

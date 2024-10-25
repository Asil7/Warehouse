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
            Warehouse newWarehouseProduct = new Warehouse(
                    productsReceiptDto.getProduct(),
                    productsReceiptDto.getQuantity(),
                    productsReceiptDto.getType());
            warehouseRepository.save(newWarehouseProduct);
        }

        ProductsReceipt newProductsReceipt = new ProductsReceipt(
                productsReceiptDto.getProduct(),
                productsReceiptDto.getQuantity(),
                productsReceiptDto.getType());
        productsReceiptRepository.save(newProductsReceipt);

        return new ApiResponse("ProductsReceipt created", true);
    }
    
    public ApiResponse updateReceivedProduct(Long id, ProductsReceiptDto productsReceiptDto) {
        Optional<ProductsReceipt> existingProductsReceiptOpt = productsReceiptRepository.findById(id);
        
        if (!existingProductsReceiptOpt.isPresent()) {
            return new ApiResponse("ProductsReceipt not found", false);
        }

        ProductsReceipt existingProductsReceipt = existingProductsReceiptOpt.get();
        
        // Calculate the quantity difference
        Long quantityDifference = productsReceiptDto.getQuantity() - existingProductsReceipt.getQuantity();

        // Update the ProductsReceipt
        existingProductsReceipt.setProduct(productsReceiptDto.getProduct());
        existingProductsReceipt.setQuantity(productsReceiptDto.getQuantity());
        existingProductsReceipt.setType(productsReceiptDto.getType());
        productsReceiptRepository.save(existingProductsReceipt);

        // Update the Warehouse
        Optional<Warehouse> existingWarehouseProductOpt = warehouseRepository.findByProduct(productsReceiptDto.getProduct());
        
        if (existingWarehouseProductOpt.isPresent()) {
            Warehouse warehouse = existingWarehouseProductOpt.get();
            warehouse.setQuantity(warehouse.getQuantity() + quantityDifference);
            warehouseRepository.save(warehouse);
        } else {
            // If the product does not exist in the warehouse, add a new entry
            Warehouse newWarehouseProduct = new Warehouse(
                    productsReceiptDto.getProduct(),
                    productsReceiptDto.getQuantity(),
                    productsReceiptDto.getType());
            warehouseRepository.save(newWarehouseProduct);
        }

        return new ApiResponse("Products Receipt updated", true);
    }


    public ApiResponse getAllReceivedProducts() {
        List<ProductsReceipt> receivedProductsList = productsReceiptRepository.findAll();
        return new ApiResponse("Received Products List", true, receivedProductsList);
    }

}

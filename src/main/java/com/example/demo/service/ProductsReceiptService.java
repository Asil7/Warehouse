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

    public ApiResponse getAllReceivedProducts() {
        List<ProductsReceipt> receivedProductsList = productsReceiptRepository.findAll();
        return new ApiResponse("Received Products List", true, receivedProductsList);
    }

}

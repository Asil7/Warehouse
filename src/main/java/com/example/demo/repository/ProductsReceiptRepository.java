package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductsReceipt;

public interface ProductsReceiptRepository extends JpaRepository<ProductsReceipt, Long> {

}

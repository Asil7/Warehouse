package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.ProductsReceipt;

public interface ProductsReceiptRepository extends JpaRepository<ProductsReceipt, Long> {

	@Query("SELECT pr FROM ProductsReceipt pr ORDER BY pr.createdAt DESC")
	List<ProductsReceipt> findAllByCreatedAtDesc();
}

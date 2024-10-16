package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>{
	boolean existsByProduct(String product);
	boolean existsByProductAndIdNot(String product, Long id);
}

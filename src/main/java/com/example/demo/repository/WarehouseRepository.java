package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Warehouse;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	boolean existsByProduct(String product);

	boolean existsByProductAndIdNot(String product, Long id);

	Optional<Warehouse> findByProduct(String product);
	
	Optional<Warehouse> findByProductAndType(String product, String type);
}

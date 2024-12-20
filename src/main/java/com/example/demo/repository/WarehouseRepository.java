package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	boolean existsByProduct(String product);

	boolean existsByProductAndIdNot(String product, Long id);
	
	List<Warehouse> findAllByOrderByCreatedAtDesc();

	@Query(value = "SELECT * FROM warehouse WHERE product = :product", nativeQuery = true)
	Optional<Warehouse> findByProduct(String product);

	@Query(value = "SELECT * FROM warehouse WHERE product = :product AND type = :type", nativeQuery = true)
	Optional<Warehouse> findByProductAndType(String product, String type);
	
	@Query("SELECT w FROM Warehouse w WHERE w.quantity < 0")
	List<Warehouse> findAllByQuantityLessThanZero();

}

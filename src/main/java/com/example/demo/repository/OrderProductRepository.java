package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

	@Query(value = "SELECT * FROM order_products op WHERE op.order_id = :orderId "
			+ "ORDER BY CASE WHEN op.type = 'kg' OR op.type = 'l' THEN 0 ELSE 1 END, "
			+ "op.quantity DESC", nativeQuery = true)
	List<OrderProduct> findByOrderIdSorted(Long orderId);

	@Query(value = "SELECT * FROM order_products WHERE id = :id", nativeQuery = true)
	Optional<OrderProduct> findById(Long id);
	
	  @Query(value = "SELECT COALESCE(SUM(op.quantity), 0) " +
              "FROM order_products op " +
              "WHERE op.order_id = :orderId " +
              "AND (op.type = 'kg' OR op.type = 'l')", 
      nativeQuery = true)
	  double findTotalQuantityByOrderId(Long orderId);

}

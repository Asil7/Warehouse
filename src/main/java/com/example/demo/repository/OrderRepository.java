package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dto.order.OrderProjection;
import com.example.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT " +
            "o.id AS id, " +
            "c.name AS company, " +
            "o.username AS username, " +
            "c.location AS location, " +
            "c.location_map AS locationMap, " +
            "o.total_weight AS totalWeight, " +
            "c.phone AS phone, " +
            "o.delivered AS delivered, " +
            "o.created_at AS createdAt " +
            "FROM orders o " +
            "JOIN companies c ON o.company_id = c.id " +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    List<OrderProjection> findAllOrders();

    @Query(value = "SELECT " +
            "o.id AS id, " +
            "c.name AS company, " +
            "o.username AS username, " +
            "c.location AS location, " +
            "c.location_map AS locationMap, " +
            "o.total_weight AS totalWeight, " +
            "c.phone AS phone, " +
            "o.delivered AS delivered, " +
            "o.created_at AS createdAt " +
            "FROM orders o " +
            "JOIN companies c ON o.company_id = c.id " +
            "WHERE o.username = :username " +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    List<OrderProjection> findOrdersByUsername(String username);

    @Query(value = "SELECT " +
            "o.id AS id, " +
            "c.name AS company, " +
            "o.username AS username, " +
            "c.location AS location, " +
            "c.location_map AS locationMap, " +
            "o.total_weight AS totalWeight, " +
            "c.phone AS phone, " +
            "o.delivered AS delivered, " +
            "o.created_at AS createdAt " +
            "FROM orders o " +
            "JOIN companies c ON o.company_id = c.id " +
            "WHERE o.id = :id", nativeQuery = true)
    OrderProjection findOrderById(Long id);

    // @Query(value = "SELECT * FROM orders o WHERE o.created_at >= :startOfDay AND
    // o.created_at < :endOfDay", nativeQuery = true)
    // List<OrderProjection> findTodayOreders(@Param("startOfDay") LocalDateTime
    // startOfDay,
    // @Param("endOfDay") LocalDateTime endOfDay);

    @Query(value = "SELECT " +
            "o.id AS id, " +
            "c.name AS company, " +
            "o.username AS username, " +
            "c.location AS location, " +
            "c.location_map AS locationMap, " +
            "o.total_weight AS totalWeight, " +
            "c.phone AS phone, " +
            "o.delivered AS delivered, " +
            "o.created_at AS createdAt " +
            "FROM orders o " +
            "JOIN companies c ON o.company_id = c.id " +
            "WHERE o.created_at >= :startOfDay AND o.created_at < :endOfDay " +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    List<OrderProjection> findTodayOreders(@Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

}

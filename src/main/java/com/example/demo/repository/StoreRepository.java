package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByProduct(String name);
    
    @Query("SELECT s FROM Store s WHERE s.received = false ORDER BY s.createdAt DESC")
    List<Store> getStoreProductList();
    
    Optional<Store> findByProduct(String product);
}

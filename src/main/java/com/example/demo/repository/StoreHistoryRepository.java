package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.StoreHistory;

public interface StoreHistoryRepository extends JpaRepository<StoreHistory, Long> {
	
	@Query("SELECT sh FROM StoreHistory sh ORDER BY sh.createdAt DESC")
	List<StoreHistory> findAllByCreatedAtDesc();
}

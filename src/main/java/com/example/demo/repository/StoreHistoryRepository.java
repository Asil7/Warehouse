package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.StoreHistory;

public interface StoreHistoryRepository extends JpaRepository<StoreHistory, Long> {

}

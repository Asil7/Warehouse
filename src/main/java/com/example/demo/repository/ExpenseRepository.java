package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Expense;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long>{
	
	List<Expense> findByUsername(String username);
}

package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Span;
import java.util.List;


public interface SpanRepository extends JpaRepository<Span, Long>{
	
	List<Span> findByUsername(String username);
}

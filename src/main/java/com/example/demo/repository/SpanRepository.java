package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Span;

public interface SpanRepository extends JpaRepository<Span, Long>{

}

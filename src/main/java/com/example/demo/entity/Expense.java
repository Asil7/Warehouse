package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.entity.template.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expense extends AbstractEntity {
	@Column(nullable = false)
	private String reason;
	@Column(nullable = false)
	private Double price;
	private String username;
	@Column(nullable = false)
	private LocalDate date;
}

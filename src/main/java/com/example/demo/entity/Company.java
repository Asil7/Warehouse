package com.example.demo.entity;

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
@Entity(name = "companies")
public class Company extends AbstractEntity {
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String additionalPhone;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private String location;
	
	@Column(nullable = false)
	private String locationMap;
	
}

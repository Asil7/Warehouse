package com.example.demo.entity.template;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.example.demo.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntity {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(updatable = false, nullable = false)
	@CreationTimestamp
	private Timestamp createdAt;
	
	@Column(nullable = false)
	@UpdateTimestamp
	private Timestamp updatedAt;
	
	@JoinColumn(updatable = false)
	@CreatedBy
	@ManyToOne
	private User createdBy;
	
	@LastModifiedBy
	@ManyToOne
	private User updatedBy;
}

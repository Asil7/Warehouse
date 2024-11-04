package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.example.demo.entity.template.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StoreHistory extends AbstractEntity {
    @Column(nullable = false)
    private String product;
    @Column(nullable = false)
    private Long quantity;
    private double price;
    private String type;
    private boolean received;
    private boolean paid;
}

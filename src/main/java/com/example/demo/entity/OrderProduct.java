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
@Entity(name = "order_products")
public class OrderProduct extends AbstractEntity {

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private Long quantity;

    // @Column(nullable = false)
    private String type; 
}

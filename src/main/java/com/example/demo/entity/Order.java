package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;

import java.util.List;

import com.example.demo.entity.template.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order extends AbstractEntity {
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private boolean delivered;

    @Column(nullable = false)
    private double totalWeight;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private List<OrderProduct> productList;

}

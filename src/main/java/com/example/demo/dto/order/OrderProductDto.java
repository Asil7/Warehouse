package com.example.demo.dto.order;

import lombok.Data;

@Data
public class OrderProductDto {
    private String product;
    private double quantity;
    private String type;
}
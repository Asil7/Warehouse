package com.example.demo.dto.order;

import lombok.Data;

@Data
public class OrderProductDto {
    private String name;
    private Long quantity;
    private String type;
}
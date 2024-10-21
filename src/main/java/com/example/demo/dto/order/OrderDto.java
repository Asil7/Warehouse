package com.example.demo.dto.order;

import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
    private String username;
    private boolean delivered;
    private List<OrderProductDto> productList;
    private Long companyId;
}

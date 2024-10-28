package com.example.demo.dto.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderProductRequest {
    private List<OrderProductDto> productList;
}

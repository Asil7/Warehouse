package com.example.demo.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private String product;
    private double quantity;
    private double price;
    private String type;
    private boolean received;
    private boolean paid;
}
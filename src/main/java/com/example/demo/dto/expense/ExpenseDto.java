package com.example.demo.dto.expense;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
	String reason;
	Double price;
	String username;
	LocalDate date;
}

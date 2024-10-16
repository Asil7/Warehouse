package com.example.demo.dto.span;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpanDto {
	String reason;
	Double price;
	String username;
	LocalDate date;
}

package com.example.demo.dto.company;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

	String name;
	
	String phone;
	
	String additionalPhone;
	
	String username;
	
	String description;
	
	String location;
	
	String locationMap;
}

package com.example.demo.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	
	@NotNull(message = "Username is empty")
	private String username;
	
	@NotNull(message = "Password is empty")
	private String password;

}

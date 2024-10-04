package com.example.demo.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
	
	@NotNull(message = "FullName is empty")
	private String fullName;
	
	@NotNull(message = "Username is empty")
	private String username;
	
	@NotNull(message = "Password is empty")
	private String password;
	
	@NotNull(message = "Role id is empty")
	private Long roleId;
	
	@NotNull(message = "Pre Password is empty")
	private String prePassword;
}

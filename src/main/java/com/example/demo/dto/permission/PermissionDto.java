package com.example.demo.dto.permission;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {

	@NotNull(message = "FullName is empty")
	String name;
	
	@NotNull(message = "Description is empty")
	String description;
}

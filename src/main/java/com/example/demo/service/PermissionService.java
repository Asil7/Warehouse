package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PermissionDto;
import com.example.demo.entity.Permission;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.PermissionRepository;

import jakarta.validation.Valid;

@Service
public class PermissionService {

	@Autowired
	PermissionRepository permissionRepository;

	public ApiResponse createPermission(@Valid PermissionDto permissionDto) {

		if (permissionRepository.existsByName(permissionDto.getName())) {
			return new ApiResponse("Permission with the name " + permissionDto.getName() + " already exists.", false);
		}

		Permission permission = new Permission();
		permission.setName(permissionDto.getName());
		permissionRepository.save(permission);
		return new ApiResponse("Permission successfully created", true);
	}
}

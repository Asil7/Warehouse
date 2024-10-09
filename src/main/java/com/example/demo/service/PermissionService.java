package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.permission.PermissionDto;
import com.example.demo.entity.Permission;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;

import jakarta.validation.Valid;

@Service
public class PermissionService {

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	RoleRepository roleRepository;

	public ApiResponse createPermission(@Valid PermissionDto permissionDto) {

		if (permissionRepository.existsByName(permissionDto.getName())) {
			return new ApiResponse("Permission with the name " + permissionDto.getName() + " already exists.", false);
		}

		Permission permission = new Permission();
		permission.setName(permissionDto.getName());
		permission.setDescription(permissionDto.getDescription());
		permissionRepository.save(permission);
		return new ApiResponse("Permission successfully created", true);
	}

	public ApiResponse editPermission(@Valid Long id, PermissionDto permissionDto) {

	    Optional<Permission> permissionOptional = permissionRepository.findById(id);

	    if (permissionOptional.isPresent()) {
	        Permission existingPermission = permissionOptional.get();

	        if (permissionRepository.existsByNameAndIdNot(permissionDto.getName(), id)) {
	            return new ApiResponse("Permission with the name " + permissionDto.getName() + " already exists.", false);
	        }

	        // Update the current permission's details
	        existingPermission.setName(permissionDto.getName());
	        existingPermission.setDescription(permissionDto.getDescription());
	        permissionRepository.save(existingPermission);

	        return new ApiResponse("Permission successfully updated", true);
	    } else {
	        return new ApiResponse("Permission not found", false);
	    }
	}


	public ApiResponse getAllPermissions() {
		List<Permission> permissionList = permissionRepository.findAll();
		return new ApiResponse("Permission List", true, permissionList);
	}

	public ApiResponse deletePermission(Long id) {
		Optional<Permission> permissOptional = permissionRepository.findById(id);
		if (permissOptional.isPresent()) {
			Permission permission = permissOptional.get();

			boolean isPermissionInUse = roleRepository.existsByPermissionsContaining(permission);

			if (isPermissionInUse) {
				return new ApiResponse("Permission cannot be deleted as it is assigned to role.", false);
			}
			permissionRepository.delete(permission);
			return new ApiResponse("Permission deleted", true);
		} else {
			return new ApiResponse("Permission not found", false);
		}
	}
}

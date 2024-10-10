package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.role.EditRoleDto;
import com.example.demo.dto.role.RoleDto;
import com.example.demo.dto.role.RoleProjection;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PermissionRepository permissionRepository;

	public ApiResponse createRole(@Valid RoleDto roleDto) {
		if (roleRepository.existsByName(roleDto.getName())) {
			return new ApiResponse("Role with the name " + roleDto.getName() + " already exists.", false);
		}

		List<Permission> permissions = permissionRepository.findAllById(roleDto.getPermissionIds());

		if (permissions.size() != roleDto.getPermissionIds().size()) {
			return new ApiResponse("One or more permissions do not exist.", false);
		}

		Role role = new Role();
		role.setName(roleDto.getName());
		role.setPermissions(permissions);
		role.setDescription(roleDto.getDescription());
		roleRepository.save(role);

		return new ApiResponse("Role successfully created", true);
	}

	public ApiResponse editRole(@Valid Long id, EditRoleDto editRoleDto) {

		Optional<Role> optionalRole = roleRepository.findById(id);
		if (optionalRole.isPresent()) {
			Role role = optionalRole.get();
			
	        if (roleRepository.existsByNameAndIdNot(editRoleDto.getName(), id)) {
	            return new ApiResponse("Role with the name " + editRoleDto.getName() + " already exists.", false);
	        }

			role.setName(editRoleDto.getName());
			role.setDescription(editRoleDto.getDescription());
			roleRepository.save(role);

			return new ApiResponse("Role successfully updated", true);
		} else {
			return new ApiResponse("Role not found", false);
		}
	}

	public ApiResponse getRoleList() {
		List<RoleProjection> roleList = roleRepository.getAllRoles();
		return new ApiResponse("Role List", true, roleList);
	}
	
	public ApiResponse getRoleById(Long id) {
		Optional<RoleProjection> roleById = roleRepository.getRoleById(id);
		if (roleById.isPresent()) {
			return new ApiResponse("Role By Id", true, roleById);
		} else {
			return new ApiResponse("Role not found", false);
		}
	}

	public ApiResponse getRolePermissions(Long roleId) {
		Optional<Role> optionalRole = roleRepository.findById(roleId);

		if (optionalRole.isPresent()) {
			Role role = optionalRole.get();
			List<Permission> permissions = role.getPermissions();

			return new ApiResponse("Role permissions", true, permissions);
		} else {
			return new ApiResponse("Role not found", false);
		}
	}

	public ApiResponse getPermissionsNotInRole(Long roleId) {
		Optional<Role> optionalRole = roleRepository.findById(roleId);

		if (optionalRole.isPresent()) {
			Role role = optionalRole.get();

			List<Permission> allPermissions = permissionRepository.findAll();

			List<Permission> rolePermissions = role.getPermissions();

			List<Permission> permissionsNotInRole = allPermissions.stream()
					.filter(permission -> !rolePermissions.contains(permission)).collect(Collectors.toList());

			return new ApiResponse("Permissions not in the role", true, permissionsNotInRole);
		} else {
			return new ApiResponse("Role not found", false);
		}
	}

	public ApiResponse addPermissionToRole(Long roleId, Long permissionId) {
		Optional<Role> optionalRole = roleRepository.findById(roleId);
		if (!optionalRole.isPresent()) {
			return new ApiResponse("Role not found", false);
		}

		Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);
		if (!optionalPermission.isPresent()) {
			return new ApiResponse("Permission not found", false);
		}

		Role role = optionalRole.get();
		Permission permission = optionalPermission.get();

		if (role.getPermissions().contains(permission)) {
			return new ApiResponse("Permission is already assigned to the role", false);
		}

		role.getPermissions().add(permission);
		roleRepository.save(role);
		return new ApiResponse("Permission added to role", true);
	}

	public ApiResponse deletePermissionFromRole(Long roleId, Long permissionId) {
		Optional<Role> optionalRole = roleRepository.findById(roleId);
		if (!optionalRole.isPresent()) {
			return new ApiResponse("Role not found", false);
		}

		Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);
		if (!optionalPermission.isPresent()) {
			return new ApiResponse("Permission not found", false);
		}

		Role role = optionalRole.get();
		Permission permission = optionalPermission.get();

		if (!role.getPermissions().contains(permission)) {
			return new ApiResponse("Permission is not assigned to the role", false);
		}

		role.getPermissions().remove(permission);
		roleRepository.save(role);
		return new ApiResponse("Permission removed from role", true);
	}

	public ApiResponse deleteRole(Long roleId) {
		Optional<Role> optionalRole = roleRepository.findById(roleId);
		if (optionalRole.isEmpty()) {
			return new ApiResponse("Role not found", false);
		}
		Role role = optionalRole.get();
		List<User> userWithRole = userRepository.findByRole(role);
		if (!userWithRole.isEmpty()) {
			return new ApiResponse("Cannot delete role. It is associated with users.", false);
		}
		roleRepository.delete(role);
		return new ApiResponse("Role deleted", true);
	}
}

package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PermissionRepository permissionRepository;

	public ApiResponse createRole(@Valid RoleDto roleDto) {
		try {
			if (roleRepository.existsByName(roleDto.getName())) {
				logger.warn("Role with name {} already exists", roleDto.getName());
				return new ApiResponse("Role with the name " + roleDto.getName() + " already exists.", false);
			}

			List<Permission> permissions = permissionRepository.findAllById(roleDto.getPermissionIds());
			if (permissions.size() != roleDto.getPermissionIds().size()) {
				logger.warn("Some permissions do not exist for the provided IDs");
				return new ApiResponse("One or more permissions do not exist.", false);
			}

			Role role = new Role();
			role.setName(roleDto.getName());
			role.setPermissions(permissions);
			role.setDescription(roleDto.getDescription());
			roleRepository.save(role);

			logger.info("Role created successfully");
			return new ApiResponse("Role successfully created", true);
		} catch (Exception e) {
			logger.error("Error in createRole: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse editRole(@Valid Long id, EditRoleDto editRoleDto) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(id);
			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", id);
				return new ApiResponse("Role not found", false);
			}

			Role role = optionalRole.get();
			if (roleRepository.existsByNameAndIdNot(editRoleDto.getName(), id)) {
				logger.warn("Role with name {} already exists for a different ID", editRoleDto.getName());
				return new ApiResponse("Role with the name " + editRoleDto.getName() + " already exists.", false);
			}

			role.setName(editRoleDto.getName());
			role.setDescription(editRoleDto.getDescription());
			roleRepository.save(role);

			logger.info("Role updated successfully");
			return new ApiResponse("Role successfully updated", true);
		} catch (Exception e) {
			logger.error("Error in editRole: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse getRoleList() {
		try {
			List<RoleProjection> roleList = roleRepository.getAllRoles();
			logger.info("Fetch role list");
			return new ApiResponse("Role List", true, roleList);
		} catch (Exception e) {
			logger.error("Error in getRoleList: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse getRoleById(Long id) {
		try {
			Optional<RoleProjection> roleById = roleRepository.getRoleById(id);
			if (roleById.isEmpty()) {
				logger.warn("Role with ID {} not found", id);
				return new ApiResponse("Role not found", false);
			}
			logger.info("Role found with ID: {}", id);
			return new ApiResponse("Role By Id", true, roleById);
		} catch (Exception e) {
			logger.error("Error in getRoleById: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse getRolePermissions(Long roleId) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(roleId);
			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", roleId);
				return new ApiResponse("Role not found", false);
			}
			List<Permission> permissions = optionalRole.get().getPermissions();
			logger.info("Permissions get for role with ID: {}", roleId);
			return new ApiResponse("Role permissions", true, permissions);
		} catch (Exception e) {
			logger.error("Error in getRolePermissions: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse getPermissionsNotInRole(Long roleId) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(roleId);
			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", roleId);
				return new ApiResponse("Role not found", false);
			}
			List<Permission> allPermissions = permissionRepository.findAll();
			List<Permission> permissionsNotInRole = allPermissions.stream()
					.filter(permission -> !optionalRole.get().getPermissions().contains(permission))
					.collect(Collectors.toList());
			logger.info("Permissions not in role with ID: {} retrieved successfully", roleId);
			return new ApiResponse("Permissions not in the role", true, permissionsNotInRole);
		} catch (Exception e) {
			logger.error("Error in getPermissionsNotInRole: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse addPermissionToRole(Long roleId, Long permissionId) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(roleId);
			Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);

			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", roleId);
				return new ApiResponse("Role not found", false);
			}
			if (optionalPermission.isEmpty()) {
				logger.warn("Permission with ID {} not found", permissionId);
				return new ApiResponse("Permission not found", false);
			}

			Role role = optionalRole.get();
			Permission permission = optionalPermission.get();

			if (role.getPermissions().contains(permission)) {
				logger.warn("Permission ID {} is already assigned to role ID {}", permissionId, roleId);
				return new ApiResponse("Permission is already assigned to the role", false);
			}

			role.getPermissions().add(permission);
			roleRepository.save(role);
			logger.info("Permission ID {} added to role ID {}", permissionId, roleId);
			return new ApiResponse("Permission added to role", true);
		} catch (Exception e) {
			logger.error("Error in addPermissionToRole: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse deletePermissionFromRole(Long roleId, Long permissionId) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(roleId);
			Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);

			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", roleId);
				return new ApiResponse("Role not found", false);
			}
			if (optionalPermission.isEmpty()) {
				logger.warn("Permission with ID {} not found", permissionId);
				return new ApiResponse("Permission not found", false);
			}

			Role role = optionalRole.get();
			Permission permission = optionalPermission.get();

			if (!role.getPermissions().contains(permission)) {
				logger.warn("Permission ID {} is not assigned to role ID {}", permissionId, roleId);
				return new ApiResponse("Permission is not assigned to the role", false);
			}

			role.getPermissions().remove(permission);
			roleRepository.save(role);
			logger.info("Permission ID {} removed from role ID {}", permissionId, roleId);
			return new ApiResponse("Permission removed from role", true);
		} catch (Exception e) {
			logger.error("Error in deletePermissionFromRole: {}", e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}

	public ApiResponse deleteRole(Long roleId) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(roleId);

			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", roleId);
				return new ApiResponse("Role not found", false);
			}

			Role role = optionalRole.get();
			List<User> userWithRole = userRepository.findByRole(role);

			if (!userWithRole.isEmpty()) {
				logger.warn("Cannot delete role with ID {} as it is associated with users", roleId);
				return new ApiResponse("Cannot delete role. It is associated with users.", false);
			}

			roleRepository.delete(role);
			logger.info("Role with ID {} deleted successfully", roleId);
			return new ApiResponse("Role deleted", true);

		} catch (Exception e) {
			logger.error("Error occurred while deleting role with ID {}: {}", roleId, e.getMessage(), e);
			return new ApiResponse("Internal server error", false);
		}
	}
}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dto.role.EditRoleDto;
import com.example.demo.dto.role.RoleDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	RoleService roleService;

	@PreAuthorize("hasAuthority('ADD_ROLE')")
	@PostMapping
	public HttpEntity<?> createRole(@Valid @RequestBody RoleDto roleDto) {
		try {
			ApiResponse apiResponse = roleService.createRole(roleDto);
			logger.info("Role create with name: {}", roleDto.getName());
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error creating role: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	// @PreAuthorize("hasAuthority('EDIT_ROLE')")
	@PutMapping("/{id}")
	public HttpEntity<?> editRole(@Valid @PathVariable Long id, @RequestBody EditRoleDto editRoleDto) {
		try {
			ApiResponse apiResponse = roleService.editRole(id, editRoleDto);
			logger.info("Role edit with ID {}", id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error editing role with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	@PreAuthorize("hasAuthority('VIEW_ROLE_LIST')")
	@GetMapping
	public HttpEntity<?> getRoleList() {
		try {
			ApiResponse apiResponse = roleService.getRoleList();
			logger.info("Fetched role list success");
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching role list: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	// @PreAuthorize("hasAuthority('VIEW_ROLE')")
	@GetMapping("/{id}")
	public HttpEntity<?> getRoleById(@PathVariable Long id) {
		logger.info("Fetching role by ID: {}", id);
		try {
			ApiResponse apiResponse = roleService.getRoleById(id);
			logger.info("Fetched role by ID {}", id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching role by ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	// @PreAuthorize("hasAuthority('VIEW_ROLE_PERMISSIONS')")
	@GetMapping("/{roleId}/permissions")
	public HttpEntity<?> getRolePermissions(@PathVariable Long roleId) {
		try {
			ApiResponse apiResponse = roleService.getRolePermissions(roleId);
			logger.info("Fetched permissions for role ID {}", roleId);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching permissions for role ID {}: {}", roleId, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	@PreAuthorize("hasAuthority('VIEW_ROLE_PERMISSIONS')")
	@GetMapping("/{roleId}/notPermissions")
	public HttpEntity<?> getPermissionsNotInRole(@PathVariable Long roleId) {
		try {
			ApiResponse apiResponse = roleService.getPermissionsNotInRole(roleId);
			logger.info("Fetched permissions not in role ID {}", roleId);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching permissions not in role ID {}: {}", roleId, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	// @PreAuthorize("hasAuthority('ADD_PERMISSION_TO_ROLE')")
	@PutMapping("/addPermission/{roleId}/{permissionId}")
	public HttpEntity<?> addPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
		try {
			ApiResponse apiResponse = roleService.addPermissionToRole(roleId, permissionId);
			logger.info("Add permission status: {}", apiResponse.isSuccess());
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error adding permission ID {} to role ID {}: {}", permissionId, roleId, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	// @PreAuthorize("hasAuthority('DELETE_PERMISSION_FROM_ROLE')")
	@PutMapping("/deletePermission/{roleId}/{permissionId}")
	public HttpEntity<?> deletePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
		try {
			ApiResponse apiResponse = roleService.deletePermissionFromRole(roleId, permissionId);
			logger.info("Delete permission status: {}", apiResponse.isSuccess());
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error deleting permission ID {} from role ID {}: {}", permissionId, roleId, e.getMessage(),
					e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	// @PreAuthorize("hasAuthority('DELETE_ROLE')")
	@DeleteMapping("/{roleId}")
	public HttpEntity<?> deleteRole(@PathVariable Long roleId) {
		try {
			ApiResponse apiResponse = roleService.deleteRole(roleId);
			logger.info("Delete role eith ID {}", roleId);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error deleting role with ID {}: {}", roleId, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

}

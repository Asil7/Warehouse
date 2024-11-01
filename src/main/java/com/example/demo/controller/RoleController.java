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

import com.example.demo.dto.role.EditRoleDto;
import com.example.demo.dto.role.RoleDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	@Autowired
	RoleService roleService;

	@PreAuthorize("hasAuthority('ADD_ROLE')")
	@PostMapping
	public HttpEntity<?> createRole(@Valid @RequestBody RoleDto roleDto) {
		ApiResponse apiResponse = roleService.createRole(roleDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('EDIT_ROLE')")
	@PutMapping("/{id}")
	public HttpEntity<?> editRole(@Valid @PathVariable Long id, @RequestBody EditRoleDto editRoleDto) {
		ApiResponse apiResponse = roleService.editRole(id, editRoleDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('VIEW_ROLE_LIST')")
	@GetMapping
	public HttpEntity<?> getRoleList() {
		ApiResponse apiResponse = roleService.getRoleList();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
//	@PreAuthorize("hasAuthority('VIEW_ROLE')")
	@GetMapping("/{id}")
	public HttpEntity<?> getRoleById(@PathVariable Long id) {
		ApiResponse apiResponse = roleService.getRoleById(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('VIEW_ROLE_PERMISSIONS')")
	@GetMapping("/{roleId}/permissions")
	public HttpEntity<?> getRolePermissions(@PathVariable Long roleId) {
		ApiResponse apiResponse = roleService.getRolePermissions(roleId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('VIEW_ROLE_PERMISSIONS')")
	@GetMapping("/{roleId}/notPermissions")
	public HttpEntity<?> getPermissionsNotInRole(@PathVariable Long roleId) {
		ApiResponse apiResponse = roleService.getPermissionsNotInRole(roleId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('ADD_PERMISSION_TO_ROLE')")
	@PutMapping("/addPermission/{roleId}/{permissionId}")
	public HttpEntity<?> addPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
		ApiResponse apiResponse = roleService.addPermissionToRole(roleId, permissionId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

//	@PreAuthorize("hasAuthority('DELETE_PERMISSION_FROM_ROLE')")
	@PutMapping("/deletePermission/{roleId}/{permissionId}")
	public HttpEntity<?> deletePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
		ApiResponse apiResponse = roleService.deletePermissionFromRole(roleId, permissionId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
//	@PreAuthorize("hasAuthority('DELETE_ROLE')")
	@DeleteMapping("/{roleId}")
	public HttpEntity<?> deleteRole(@PathVariable Long roleId) {
		ApiResponse apiResponse = roleService.deleteRole(roleId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

}

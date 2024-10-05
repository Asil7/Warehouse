package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PermissionDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.PermissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

	@Autowired
	PermissionService permissionService;

	@PreAuthorize("hasAuthority('ADD_PERMISSION')")
	@PostMapping
	public HttpEntity<?> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
		ApiResponse apiResponse = permissionService.createPermission(permissionDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
}

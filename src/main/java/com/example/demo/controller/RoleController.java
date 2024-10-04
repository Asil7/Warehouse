package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RoleDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	@Autowired
	RoleService roleService;

	@PostMapping
	public HttpEntity<?> createRole(@Valid @RequestBody RoleDto roleDto) {
		ApiResponse apiResponse = roleService.createRole(roleDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
}

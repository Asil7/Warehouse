package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RegisterDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/register")
	public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
		ApiResponse apiResponse = authService.registerUser(registerDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
}

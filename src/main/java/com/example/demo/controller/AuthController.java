package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.user.LoginDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            ApiResponse apiResponse = authService.loginUser(loginDto);
            if (apiResponse.isSuccess()) {
                logger.info("User logged in successfully: {}", loginDto.getUsername());
            } else {
                logger.warn("Failed login attempt for user: {}", loginDto.getUsername());
            }
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error logging in user {}: ", loginDto.getUsername(), e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while logging in", false));
        }
    }
}

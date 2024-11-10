package com.example.demo.controller;

import org.springframework.http.HttpEntity;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserEditDto;
import com.example.demo.entity.enums.Status;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@PreAuthorize("hasAuthority('VIEW_USER_LIST')")
	@GetMapping()
	public HttpEntity<?> getUserList() {
		try {
			ApiResponse apiResponse = userService.getAllUsers();
			logger.info("Fetched user list successfully.");
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching user list: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error fetching user list", false));
		}
	}

	// @PreAuthorize("hasAuthority('VIEW_USER')")
	public HttpEntity<?> getUserById(@PathVariable Long id) {
		try {
			ApiResponse apiResponse = userService.getUserById(id);
			logger.info("Fetched user with ID {} successfully.", id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching user by ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error fetching user by ID", false));
		}
	}

	@PreAuthorize("hasAuthority('ADD_USER')")
	@PostMapping()
	public HttpEntity<?> addUser(@RequestBody UserDto userDto) {
		try {
			ApiResponse apiResponse = userService.addUser(userDto);
			logger.info("User added successfully.");
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error adding user: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error adding user", false));
		}
	}

	// @PreAuthorize("hasAuthority('EDIT_USER')")
	@PutMapping("/{id}")
	public HttpEntity<?> editUser(@PathVariable Long id, @RequestBody UserEditDto userEditDto) {
		try {
			ApiResponse apiResponse = userService.editUser(id, userEditDto);
			logger.info("User with ID {} edited successfully.", id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error editing user with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error editing user", false));
		}
	}

	// @PreAuthorize("hasAuthority('EDIT_USER_STATUS')")
	@PutMapping("/status/{id}/{status}")
	public HttpEntity<?> updateStatus(@PathVariable Long id, @PathVariable Status status) {
		try {
			ApiResponse apiResponse = userService.updateStatus(id, status);
			logger.info("Updated status for user ID {} to {}.", id, status);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error updating status for user ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error updating status", false));
		}
	}

	// @PreAuthorize("hasAuthority('EDIT_USER_PASSWORD')")
	@PutMapping("/password/{id}")
	public HttpEntity<?> updatePassword(@PathVariable Long id, @RequestBody String password) {
		try {
			ApiResponse apiResponse = userService.updatePassword(id, password);
			logger.info("Password updated for user ID {}.", id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error updating password for user ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error updating password", false));
		}
	}

	// @PreAuthorize("hasAuthority('DELETE_USER')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteUser(@PathVariable Long id) {
		try {
			ApiResponse apiResponse = userService.deleteUser(id);
			logger.info("User with ID {} deleted successfully.", id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error deleting user", false));
		}
	}

	@GetMapping("/{userId}/calculate-salary")
	public HttpEntity<?> calculateUserSalary(@PathVariable Long userId,
			@RequestParam("givenDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate givenDate) {
		try {
			ApiResponse apiResponse = userService.calculateUserSalary(userId, givenDate);
			logger.info("Calculated salary for user ID {} for date {}", userId, givenDate);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error calculating salary for user ID {}: {}", userId, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error calculating salary", false));
		}
	}

	@PostMapping("/give-salary")
	public HttpEntity<?> giveSalary(@RequestParam String username, @RequestParam Double salary,
			@RequestParam Double givenSalary) {
		try {
			ApiResponse apiResponse = userService.giveSalary(username, salary, givenSalary);
			logger.info("Salary given to user {}", username);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error giving salary to user {}: {}", username, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error giving salary", false));
		}
	}
}

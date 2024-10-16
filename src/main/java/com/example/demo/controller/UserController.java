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

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@PreAuthorize("hasAuthority('VIEW_USER_LIST')")
	@GetMapping()
	public HttpEntity<?> getUserList() {
		ApiResponse apiResponse = userService.getAllUsers();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('VIEW_USER')")
	@GetMapping("/{id}")
	public HttpEntity<?> getUserById(@PathVariable Long id) {
		ApiResponse apiResponse = userService.getUserById(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('ADD_USER')")
	@PostMapping()
	public HttpEntity<?> addUser(@RequestBody UserDto userDto) {
		ApiResponse apiResponse = userService.addUser(userDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('EDIT_USER')")
	@PutMapping("/{id}")
	public HttpEntity<?> editUser(@PathVariable Long id, @RequestBody UserEditDto userEditDto) {
		ApiResponse apiResponse = userService.editUser(id, userEditDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('EDIT_USER_STATUS')")
	@PutMapping("/status/{id}/{status}")
	public HttpEntity<?> updateStatus(@PathVariable Long id, @PathVariable Status status) {
		ApiResponse apiResponse = userService.updateStatus(id, status);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('EDIT_USER_PASSWORD')")
	@PutMapping("/password/{id}")
	public HttpEntity<?> updateStatus(@PathVariable Long id, @RequestBody String password) {
		ApiResponse apiResponse = userService.updatePassword(id, password);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('DELETE_USER')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteUser(@PathVariable Long id) {
		ApiResponse apiResponse = userService.deleteUser(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@GetMapping("/{userId}/calculate-salary")
	public HttpEntity<?> calculateUserSalary(@PathVariable Long userId, @RequestParam("givenDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate givenDate){
		ApiResponse apiResponse = userService.calculateUserSalary(userId, givenDate);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('GIVE_SALARY')")
	@PostMapping("/give-salary")
	public HttpEntity<?> giveSalary(@RequestParam String username, @RequestParam Double salary, Double givenSalary){
		ApiResponse apiResponse = userService.giveSalary(username, salary, givenSalary);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
}














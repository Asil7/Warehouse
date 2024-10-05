package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.Status;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

		public ApiResponse addUser(UserDto userDto) {
		Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
		if (optionalRole.isEmpty()) {
			return new ApiResponse("Role not found", false);
		}
		if (userRepository.existsByUsername(userDto.getUsername()))
			return new ApiResponse("This user already exists", false);
		User user = new User();
		user.setFullName(userDto.getFullName());
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setStatus(Status.INACTIVE);
		userRepository.save(user);
		return new ApiResponse("user successfully created", true);
	}

		public ApiResponse editUser(Long id, UserDto userDto) {
		Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
		if (optionalRole.isEmpty()) {
			return new ApiResponse("Role not found", false);
		}
		if (userRepository.existsByUsername(userDto.getUsername()))
			return new ApiResponse("This user already exists", false);
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setFullName(userDto.getFullName());
			user.setUsername(userDto.getUsername());
			user.setPassword(userDto.getPassword());
			user.setRole(optionalRole.get());
			userRepository.save(user);
			return new ApiResponse("user successfully updated", true);
		} else {
			return new ApiResponse("User not found", false);
		}
	}

	public ApiResponse updateStatus(Long id, Status status) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setStatus(status);
			userRepository.save(user);
			return new ApiResponse("User status updated successfully", true);
		} else {
			return new ApiResponse("User not found", false);
		}
	}

	public ApiResponse deleteUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			userRepository.deleteById(id);
			return new ApiResponse("User successfully deleted", true);
		} else {
			return new ApiResponse("User not found", false);
		}
	}

}

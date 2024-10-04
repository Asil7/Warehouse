package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.Status;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;

import jakarta.validation.Valid;

@Service
public class AuthService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired 
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtProvider jwtProvider;

	public ApiResponse registerUser(RegisterDto registerDto) {
		Optional<Role> optionalRole = roleRepository.findById(registerDto.getRoleId());
		if (optionalRole.isEmpty()) {
			return new ApiResponse("Role not found", false);
		}
		if (!registerDto.getPassword().equals(registerDto.getPrePassword()))
			return new ApiResponse("Pre Password is invalid", false);
		if (userRepository.existsByUsername(registerDto.getUsername()))
			return new ApiResponse("This user already exists", false);
		User user = new User(registerDto.getFullName(), registerDto.getUsername(),
				passwordEncoder.encode(registerDto.getPassword()), optionalRole.get(), Status.INACTIVE);
		userRepository.save(user);
		return new ApiResponse("Successfully", true);
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isPresent())
			return optionalUser.get();
		throw new UsernameNotFoundException(username + " not found");
	}

	public ApiResponse loginUser(@Valid LoginDto loginDto) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		User user = (User)authenticate.getPrincipal();
		String token = jwtProvider.generateToken(user.getUsername(), user.getRole());
		return new ApiResponse("Token", true, token);
	}
}

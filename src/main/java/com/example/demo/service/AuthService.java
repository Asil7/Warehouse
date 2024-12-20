package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.user.LoginDto;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.Status;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;

import jakarta.validation.Valid;

@Service
public class AuthService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()) {
                logger.info("User {} found", username);
                return optionalUser.get();
            } else {
                logger.warn("User {} not found", username);
                throw new UsernameNotFoundException(username + " not found");
            }
        } catch (Exception e) {
            logger.error("Error loading user by username {}: {}", username, e.getMessage(), e);
            throw new UsernameNotFoundException("An error occurred while loading user: " + username, e);
        }
    }

    public ApiResponse loginUser(@Valid LoginDto loginDto) {
        
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            
            User user = (User) authenticate.getPrincipal();

            if (user.getStatus() == Status.INACTIVE) {
                logger.warn("Inactive user tried to login: {}", loginDto.getUsername());
                return new ApiResponse("User is inactive", false);
            }

            String token = jwtProvider.generateToken(user.getUsername(), user.getRole());
            logger.info("Token generated successfully for user: {}", loginDto.getUsername());

            return new ApiResponse("Token generated successfully", true, token);

        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for username: {}", loginDto.getUsername());
            return new ApiResponse("Invalid username or password", false);
            
        } catch (DisabledException e) {
            logger.warn("User account disabled for username: {}", loginDto.getUsername());
            return new ApiResponse("User account is disabled", false);
            
        } catch (Exception e) {
            logger.error("An error occurred during login for username {}: {}", loginDto.getUsername(), e.getMessage(), e);
            return new ApiResponse("An error occurred during login", false);
        }
    }
}

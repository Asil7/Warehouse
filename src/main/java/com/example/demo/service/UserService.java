package com.example.demo.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserEditDto;
import com.example.demo.dto.user.UserProjection;
import com.example.demo.entity.Role;
import com.example.demo.entity.Span;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.Status;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SpanRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SpanRepository spanRepository;

	public ApiResponse getAllUsers() {
		List<UserProjection> userList = userRepository.getAllUsers();
		return new ApiResponse("User list", true, userList);
	}

	public ApiResponse addUser(UserDto userDto) {

		Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
		if (optionalRole.isEmpty()) {
			return new ApiResponse("Role not found", false);
		}

		String encodedPassword = passwordEncoder.encode(userDto.getPassword());

		User newUser = new User();
		newUser.setFullName(userDto.getFullName());
		newUser.setUsername(userDto.getUsername());
		newUser.setPassword(encodedPassword);
		newUser.setSalary(userDto.getSalary());
		newUser.setPhone(userDto.getPhone());
		newUser.setDateOfEmployment(userDto.getDateOfEmployment());
		newUser.setRole(optionalRole.get());
		newUser.setStatus(Status.INACTIVE);

		userRepository.save(newUser);

		return new ApiResponse("User created", true);
	}

	public ApiResponse editUser(Long id, UserEditDto userEditDto) {
		Optional<Role> optionalRole = roleRepository.findById(userEditDto.getRoleId());
		if (optionalRole.isEmpty()) {
			return new ApiResponse("Role not found", false);
		}

		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (!user.getUsername().equals(userEditDto.getUsername())
					&& userRepository.existsByUsername(userEditDto.getUsername())) {
				return new ApiResponse("This user already exists", false);
			}
			
			user.setFullName(userEditDto.getFullName());
			user.setUsername(userEditDto.getUsername());
			user.setSalary(userEditDto.getSalary());
			user.setPhone(userEditDto.getPhone());
			user.setDateOfEmployment(userEditDto.getDateOfEmployment());
			user.setRole(optionalRole.get());
			userRepository.save(user);

			return new ApiResponse("User updated", true);
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
			return new ApiResponse("User status updated", true);
		} else {
			return new ApiResponse("User not found", false);
		}
	}
	
	public ApiResponse updatePassword(Long id, String password) {
		Optional<User> optionalUser = userRepository.findById(id);
		String encodedPassword = passwordEncoder.encode(password);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setPassword(encodedPassword);
			userRepository.save(user);
			return new ApiResponse("User password updated", true);
		} else {
			return new ApiResponse("User not found", false);
		}
	}

	public ApiResponse deleteUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			userRepository.deleteById(id);
			return new ApiResponse("User deleted", true);
		} else {
			return new ApiResponse("User not found", false);
		}
	}

	public ApiResponse getUserById(Long id) {
		Optional<UserProjection> userById = userRepository.getUserById(id);
		if (userById.isPresent()) {
			return new ApiResponse("User by id", true, userById);
		} else {
			return new ApiResponse("User not found", false);
		}
	}

	public ApiResponse calculateUserSalary(Long userId, LocalDate givenDate) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("User not found", false);
        }

        User user = optionalUser.get();

        double dailySalary = user.getSalary() / 30;

        long daysWorked = ChronoUnit.DAYS.between(user.getDateOfEmployment(), givenDate);
        if (daysWorked < 0) {
            return new ApiResponse("Invalid date, given date is before date of employment", false);
        }

        double totalEarnedSalary = dailySalary * daysWorked;

        List<Span> userSpans = spanRepository.findByUsername(user.getUsername());
        double totalSpansPrice = userSpans.stream()
                                          .mapToDouble(Span::getPrice)
                                          .sum();

        double finalAmount = totalEarnedSalary - totalSpansPrice;
        
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedAmount = df.format(finalAmount);

		return new ApiResponse("Final salary calculated", true, formattedAmount);
	}

	public ApiResponse giveSalary(String username, Double salary, Double givenSalary) {
		if (salary.equals(givenSalary)) {
			Optional<User> optionalUser = userRepository.findByUsername(username);
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				user.setDateOfEmployment(null);
				userRepository.save(user);

				List<Span> spans = spanRepository.findByUsername(username);
				if (!spans.isEmpty()) {
					spanRepository.deleteAll();
				}
				return new ApiResponse("Salary given, date of employment removed and spans deleted", true);
			} else {
				return new ApiResponse("User not found", false);
			}
		} else {
			return new ApiResponse("Salary does not match the given salary", false);
		}
	}
}





































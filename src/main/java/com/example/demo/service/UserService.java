package com.example.demo.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.user.FirebaseTokenRequest;
import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserEditDto;
import com.example.demo.dto.user.UserProjection;
import com.example.demo.entity.Role;
import com.example.demo.entity.Expense;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.Status;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ExpenseRepository expenseRepository;

	public ApiResponse getAllUsers() {
		try {
			List<UserProjection> userList = userRepository.getAllUsers();
			logger.info("Fetched all users successfully.");
			return new ApiResponse("User list", true, userList);
		} catch (Exception e) {
			logger.error("Error fetching all users: {}", e.getMessage(), e);
			return new ApiResponse("Error fetching users", false);
		}
	}

	public ApiResponse addUser(UserDto userDto) {
		try {

			if (userRepository.existsByUsername(userDto.getUsername())) {
				logger.warn("Username {} already exists", userDto.getUsername());
				return new ApiResponse("Username already exists", false);
			}

			Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", userDto.getRoleId());
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
			logger.info("User {} created successfully", userDto.getUsername());

			return new ApiResponse("User created", true);
		} catch (Exception e) {
			logger.error("Error adding user: {}", e.getMessage(), e);
			return new ApiResponse("Error creating user", false);
		}
	}

	public ApiResponse editUser(Long id, UserEditDto userEditDto) {
		try {
			Optional<Role> optionalRole = roleRepository.findById(userEditDto.getRoleId());
			if (optionalRole.isEmpty()) {
				logger.warn("Role with ID {} not found", userEditDto.getRoleId());
				return new ApiResponse("Role not found", false);
			}

			Optional<User> optionalUser = userRepository.findById(id);
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				if (!user.getUsername().equals(userEditDto.getUsername()) &&
						userRepository.existsByUsername(userEditDto.getUsername())) {
					logger.warn("Username {} already exists", userEditDto.getUsername());
					return new ApiResponse("This user already exists", false);
				}

				user.setFullName(userEditDto.getFullName());
				user.setUsername(userEditDto.getUsername());
				user.setSalary(userEditDto.getSalary());
				user.setPhone(userEditDto.getPhone());
				user.setDateOfEmployment(userEditDto.getDateOfEmployment());
				user.setRole(optionalRole.get());
				userRepository.save(user);
				logger.info("User with ID {} updated successfully", id);

				return new ApiResponse("User updated", true);
			} else {
				logger.warn("User with ID {} not found", id);
				return new ApiResponse("User not found", false);
			}
		} catch (Exception e) {
			logger.error("Error editing user with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating user", false);
		}
	}

	public ApiResponse updateStatus(Long id, Status status) {
		try {
			Optional<User> optionalUser = userRepository.findById(id);
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				user.setStatus(status);
				userRepository.save(user);
				logger.info("Status of user with ID {} updated to {}", id, status);
				return new ApiResponse("User status updated", true);
			} else {
				logger.warn("User with ID {} not found", id);
				return new ApiResponse("User not found", false);
			}
		} catch (Exception e) {
			logger.error("Error updating status for user with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating user status", false);
		}
	}

	public ApiResponse updatePassword(Long id, String password) {
		try {
			Optional<User> optionalUser = userRepository.findById(id);
			if (optionalUser.isPresent()) {
				String encodedPassword = passwordEncoder.encode(password);
				User user = optionalUser.get();
				user.setPassword(encodedPassword);
				userRepository.save(user);
				logger.info("Password for user with ID {} updated successfully", id);
				return new ApiResponse("User password updated", true);
			} else {
				logger.warn("User with ID {} not found", id);
				return new ApiResponse("User not found", false);
			}
		} catch (Exception e) {
			logger.error("Error updating password for user with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating password", false);
		}
	}

	public ApiResponse deleteUser(Long id) {
		try {
			Optional<User> optionalUser = userRepository.findById(id);
			if (optionalUser.isPresent()) {
				userRepository.deleteById(id);
				logger.info("User with ID {} deleted successfully", id);
				return new ApiResponse("User deleted", true);
			} else {
				logger.warn("User with ID {} not found", id);
				return new ApiResponse("User not found", false);
			}
		} catch (Exception e) {
			logger.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error deleting user", false);
		}
	}

	public ApiResponse getUserById(Long id) {
		try {
			Optional<UserProjection> userById = userRepository.getUserById(id);
			if (userById.isPresent()) {
				logger.info("User with ID {} found", id);
				return new ApiResponse("User by id", true, userById);
			} else {
				logger.warn("User with ID {} not found", id);
				return new ApiResponse("User not found", false);
			}
		} catch (Exception e) {
			logger.error("Error fetching user with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error fetching user", false);
		}
	}

	public ApiResponse calculateUserSalary(Long userId, LocalDate givenDate) {
		try {
			Optional<User> optionalUser = userRepository.findById(userId);
			if (optionalUser.isEmpty()) {
				logger.warn("User with ID {} not found", userId);
				return new ApiResponse("User not found", false);
			}

			User user = optionalUser.get();
			if (user.getDateOfEmployment() == null) {
				logger.warn("Date of employment not found for user ID {}", userId);
				return new ApiResponse("Date Of Employment not found", false);
			}

			double dailySalary = user.getSalary() / 30;
			long daysWorked = ChronoUnit.DAYS.between(user.getDateOfEmployment(), givenDate) + 1;
			if (daysWorked < 0) {
				logger.warn("Invalid given date: {} is before date of employment for user ID {}", givenDate, userId);
				return new ApiResponse("Invalid date, given date is before date of employment", false);
			}

			double totalEarnedSalary = dailySalary * daysWorked;
			List<Expense> userExpenses = expenseRepository.findByUsername(user.getUsername());
			double totalExpensesPrice = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
			double finalAmount = totalEarnedSalary - totalExpensesPrice;

			DecimalFormat df = new DecimalFormat("#.00");
			String formattedAmount = df.format(finalAmount);

			logger.info("Calculated final salary for user ID {}: {}", userId, formattedAmount);
			return new ApiResponse("Final salary calculated", true, formattedAmount);
		} catch (Exception e) {
			logger.error("Error calculating salary for user ID {}: {}", userId, e.getMessage(), e);
			return new ApiResponse("Error calculating salary", false);
		}
	}

	public ApiResponse giveSalary(String username, Double salary, Double givenSalary) {
		try {
			if (salary.equals(givenSalary)) {
				Optional<User> optionalUser = userRepository.findByUsername(username);
				if (optionalUser.isPresent()) {
					User user = optionalUser.get();

					logger.info("Giving salary to user {}. Setting date of employment to null.", username);
					user.setDateOfEmployment(null);
					userRepository.save(user);

					List<Expense> expenses = expenseRepository.findByUsername(username);
					if (!expenses.isEmpty()) {
						logger.info("Deleting {} expenses for user {}", expenses.size(), username);
						expenseRepository.deleteAll(expenses);
					}

					logger.info("Salary given successfully to user {}", username);
					return new ApiResponse("Salary given", true);
				} else {
					logger.warn("User with username {} not found", username);
					return new ApiResponse("User not found", false);
				}
			} else {
				logger.warn("Salary {} does not match the given salary {}", salary, givenSalary);
				return new ApiResponse("Salary does not match the given salary", false);
			}
		} catch (Exception e) {
			logger.error("Error while giving salary to user {}: {}", username, e.getMessage(), e);
			return new ApiResponse("Error giving salary", false);
		}
	}
	
	public ApiResponse updateFirebaseToken(FirebaseTokenRequest firebaseTokenRequest) {
		Optional<User> optionalUser = userRepository.findByUsername(firebaseTokenRequest.getUsername());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setFirebaseToken(firebaseTokenRequest.getFirebaseToken());
			userRepository.save(user);
			return new ApiResponse("Firebase token updated", true);
		}
		return new ApiResponse("User not found", false);
	}

}

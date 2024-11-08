package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dto.expense.ExpenseDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

	private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

	@Autowired
	ExpenseService expenseService;

	@PreAuthorize("hasAuthority('ADD_EXPENSE')")
	@PostMapping
	public HttpEntity<?> createExpense(@Valid @RequestBody ExpenseDto expenseDto) {
		try {
			ApiResponse apiResponse = expenseService.createExpense(expenseDto);
			logger.info("Expense created successfully for user: {}", expenseDto.getUsername());
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error creating expense for user {}: {}", expenseDto.getUsername(), e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error creating expense", false));
		}
	}

//	@PreAuthorize("hasAuthority('EDIT_EXPENSE')")
	@PutMapping("/{id}")
	public HttpEntity<?> editExpense(@Valid @PathVariable Long id, @RequestBody ExpenseDto expenseDto) {
		try {
			ApiResponse apiResponse = expenseService.editExpense(id, expenseDto);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error updating expense with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error updating expense", false));
		}

	}

	@PreAuthorize("hasAuthority('VIEW_EXPENSE_LIST')")
	@GetMapping
	public HttpEntity<?> getExpense() {
		try {
			ApiResponse apiResponse = expenseService.getAllExpense();
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching expense list: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error fetching expense list", false));
		}

	}

//	@PreAuthorize("hasAuthority('DELETE_EXPENSE')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteExpense(@PathVariable Long id) {
		try {
			ApiResponse apiResponse = expenseService.deleteExpense(id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error deleting expense with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error deleting expense", false));
		}
	}

//	@PreAuthorize("hasAuthority('VIEW_EXPENSE_BY_USERNAME')")
	@GetMapping("/{username}")
	public HttpEntity<?> getExpenseByUsername(@PathVariable String username) {
		try {
			ApiResponse apiResponse = expenseService.getExpenseByUsername(username);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error fetching expenses for username {}: {}", username, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Error fetching expenses by username", false));
		}
	}
}

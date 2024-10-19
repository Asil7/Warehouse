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

import com.example.demo.dto.expense.ExpenseDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
	
	@Autowired
	ExpenseService expenseService;

	@PreAuthorize("hasAuthority('ADD_EXPENSE')")
	@PostMapping
	public HttpEntity<?> createExpense(@Valid @RequestBody ExpenseDto expenseDto) {
		ApiResponse apiResponse = expenseService.createExpense(expenseDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('EDIT_EXPENSE')")
	@PutMapping("/{id}")
	public HttpEntity<?> editExpense(@Valid @PathVariable Long id, @RequestBody ExpenseDto expenseDto) {
		ApiResponse apiResponse = expenseService.editExpense(id, expenseDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('VIEW_EXPENSE_LIST')")
	@GetMapping
	public HttpEntity<?> getExpense() {
		ApiResponse apiResponse = expenseService.getAllExpense();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('DELETE_EXPENSE')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteExpense(@PathVariable Long id) {
		ApiResponse apiResponse = expenseService.deleteExpense(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('VIEW_EXPENSE_BY_USERNAME')")
	@GetMapping("/{username}")
	public HttpEntity<?> getExpenseByUsername(@PathVariable String username) {
		ApiResponse apiResponse = expenseService.getExpenseByUsername(username);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
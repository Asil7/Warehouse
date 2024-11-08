package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.expense.ExpenseDto;
import com.example.demo.entity.Expense;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.ExpenseRepository;

import jakarta.validation.Valid;

@Service
public class ExpenseService {

	private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

	@Autowired
	ExpenseRepository expenseRepository;

	public ApiResponse createExpense(@Valid ExpenseDto expenseDto) {
		try {
			Expense expense = new Expense();

			expense.setReason(expenseDto.getReason());
			expense.setPrice(expenseDto.getPrice());
			expense.setUsername(expenseDto.getUsername());
			expense.setDate(expenseDto.getDate());

			expenseRepository.save(expense);
			logger.info("Expense created successfully for user: {}", expenseDto.getUsername());
			return new ApiResponse("Expense created", true);
		} catch (Exception e) {
			logger.error("Error creating Expense for user {}: {}", expenseDto.getUsername(), e.getMessage(), e);
			return new ApiResponse("Error creating Expense", false);
		}
	}

	public ApiResponse editExpense(@Valid Long id, ExpenseDto expenseDto) {
		try {
			Optional<Expense> expenseOptional = expenseRepository.findById(id);

			if (expenseOptional.isPresent()) {
				Expense existingExpense = expenseOptional.get();

				existingExpense.setReason(expenseDto.getReason());
				existingExpense.setPrice(expenseDto.getPrice());
				existingExpense.setUsername(expenseDto.getUsername());
				existingExpense.setDate(expenseDto.getDate());

				expenseRepository.save(existingExpense);
				logger.info("Expense with ID {} updated successfully", id);
				return new ApiResponse("Expense updated", true);
			} else {
				logger.warn("Expense with ID {} not found for update", id);
				return new ApiResponse("Expense not found", false);
			}
		} catch (Exception e) {
			logger.error("Error updating Expense with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating Expense", false);
		}
	}

	public ApiResponse getAllExpense() {
		try {
			List<Expense> expenseList = expenseRepository.findAll();
			logger.info("Fetched {} expenses", expenseList.size());
			return new ApiResponse("Expense List", true, expenseList);
		} catch (Exception e) {
			logger.error("Error fetching all expenses: {}", e.getMessage(), e);
			return new ApiResponse("Error fetching all expenses", false);
		}
	}

	public ApiResponse getExpenseByUsername(String username) {
		try {
			List<Expense> findByUsername = expenseRepository.findByUsername(username);
			logger.info("Fetched {} expenses for user: {}", findByUsername.size(), username);
			return new ApiResponse("Expense by username", true, findByUsername);
		} catch (Exception e) {
			logger.error("Error fetching expenses for username {}: {}", username, e.getMessage(), e);
			return new ApiResponse("An error occurred while fetching expenses", false);
		}
	}

	public ApiResponse deleteExpense(Long id) {
		try {
			Optional<Expense> expenseOptional = expenseRepository.findById(id);

			if (expenseOptional.isPresent()) {
				Expense expense = expenseOptional.get();

				expenseRepository.delete(expense);
				logger.info("Expense with ID {} deleted successfully", id);
				return new ApiResponse("Expense deleted", true);
			} else {
				logger.warn("Expense with ID {} not found for deletion", id);
				return new ApiResponse("Expense not found", false);
			}
		} catch (Exception e) {
			logger.error("Error deleting Expense with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error deleting Expense", false);
		}
	}
}

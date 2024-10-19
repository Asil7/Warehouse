package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.expense.ExpenseDto;
import com.example.demo.entity.Expense;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.ExpenseRepository;

import jakarta.validation.Valid;

@Service
public class ExpenseService {

	@Autowired
	ExpenseRepository expenseRepository;

	public ApiResponse createExpense(@Valid ExpenseDto expenseDto) {

		Expense expense = new Expense();

		expense.setReason(expenseDto.getReason());
		expense.setPrice(expenseDto.getPrice());
		expense.setUsername(expenseDto.getUsername());
		expense.setDate(expenseDto.getDate());

		expenseRepository.save(expense);

		return new ApiResponse("Expense created", true);
	}

	public ApiResponse editExpense(@Valid Long id, ExpenseDto expenseDto) {

		Optional<Expense> expenseOptional = expenseRepository.findById(id);

		if (expenseOptional.isPresent()) {
			Expense existingExpense = expenseOptional.get();

			existingExpense.setReason(expenseDto.getReason());
			existingExpense.setPrice(expenseDto.getPrice());
			existingExpense.setUsername(expenseDto.getUsername());
			existingExpense.setDate(expenseDto.getDate());
			expenseRepository.save(existingExpense);

			return new ApiResponse("Expense updated", true);
		} else {
			return new ApiResponse("Expense not found", false);
		}
	}

	public ApiResponse getAllExpense() {
		List<Expense> expenseList = expenseRepository.findAll();
		return new ApiResponse("Expense List", true, expenseList);
	}

	public ApiResponse getExpenseByUsername(String username) {
		try {
			List<Expense> findByUsername = expenseRepository.findByUsername(username);
			return new ApiResponse("Expense by username", true, findByUsername);
		} catch (Exception e) {
			return new ApiResponse("An error occurred while fetching expense", false);
		}
	}

	public ApiResponse deleteExpense(Long id) {
		Optional<Expense> expenseOptional = expenseRepository.findById(id);
		if (expenseOptional.isPresent()) {
			Expense expense = expenseOptional.get();

			expenseRepository.delete(expense);
			return new ApiResponse("Expense deleted", true);
		} else {
			return new ApiResponse("Expense not found", false);
		}
	}
}

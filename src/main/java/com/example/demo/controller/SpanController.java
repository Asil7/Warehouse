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

import com.example.demo.dto.span.SpanDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.SpanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/span")
public class SpanController {
	
	@Autowired
	SpanService spanService;

	@PreAuthorize("hasAuthority('ADD_SPAN')")
	@PostMapping
	public HttpEntity<?> createPSpan(@Valid @RequestBody SpanDto spanDto) {
		ApiResponse apiResponse = spanService.createSpan(spanDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('EDIT_SPAN')")
	@PutMapping("/{id}")
	public HttpEntity<?> editSpan(@Valid @PathVariable Long id, @RequestBody SpanDto spanDto) {
		ApiResponse apiResponse = spanService.editSpan(id, spanDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('VIEW_SPAN_LIST')")
	@GetMapping
	public HttpEntity<?> getSpan() {
		ApiResponse apiResponse = spanService.getAllSpan();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@PreAuthorize("hasAuthority('DELETE_SPAN')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteSpan(@PathVariable Long id) {
		ApiResponse apiResponse = spanService.deleteSpan(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
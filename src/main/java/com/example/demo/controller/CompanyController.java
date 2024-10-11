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

import com.example.demo.dto.company.CompanyDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
	
	@Autowired
	CompanyService companyService;

	@PreAuthorize("hasAuthority('ADD_COMPANY')")
	@PostMapping
	public HttpEntity<?> createCompany(@Valid @RequestBody CompanyDto companyDto) {
		ApiResponse apiResponse = companyService.createCompany(companyDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('EDIT_COMPANY')")
	@PutMapping("/{id}")
	public HttpEntity<?> updateCompany(@Valid @PathVariable Long id, @RequestBody CompanyDto companyDto) {
		ApiResponse apiResponse = companyService.updateCompany(id, companyDto);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('VIEW_COMPANY_LIST')")
	@GetMapping
	public HttpEntity<?> getCompanyList(){
		ApiResponse apiResponse = companyService.getAllCompany();
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('VIEW_COMPANY')")
	@GetMapping("/{id}")
	public HttpEntity<?> getCompanyById(@PathVariable Long id){
		ApiResponse apiResponse = companyService.getCompanyById(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
	@PreAuthorize("hasAuthority('DELETE_COMPANY')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteCompany(@PathVariable Long id){
		ApiResponse apiResponse = companyService.deleteCompany(id);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
	
}























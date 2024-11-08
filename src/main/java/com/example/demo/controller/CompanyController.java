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

import com.example.demo.dto.company.CompanyDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	CompanyService companyService;

	@PreAuthorize("hasAuthority('ADD_COMPANY')")
	@PostMapping
	public HttpEntity<?> createCompany(@Valid @RequestBody CompanyDto companyDto) {
		try {
			ApiResponse apiResponse = companyService.createCompany(companyDto);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error while creating company: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

//	@PreAuthorize("hasAuthority('EDIT_COMPANY')")
	@PutMapping("/{id}")
	public HttpEntity<?> updateCompany(@Valid @PathVariable Long id, @RequestBody CompanyDto companyDto) {
		try {
			ApiResponse apiResponse = companyService.updateCompany(id, companyDto);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error while updating company with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

	@PreAuthorize("hasAuthority('VIEW_COMPANY_LIST')")
	@GetMapping
	public HttpEntity<?> getCompanyList() {
		try {
			ApiResponse apiResponse = companyService.getAllCompany();
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error while fetching company list: {}", e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

//	@PreAuthorize("hasAuthority('VIEW_COMPANY')")
	@GetMapping("/{id}")
	public HttpEntity<?> getCompanyById(@PathVariable Long id) {
		try {
			ApiResponse apiResponse = companyService.getCompanyById(id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error while fetching company with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

//	@PreAuthorize("hasAuthority('DELETE_COMPANY')")
	@DeleteMapping("/{id}")
	public HttpEntity<?> deleteCompany(@PathVariable Long id) {
		try {
			ApiResponse apiResponse = companyService.deleteCompany(id);
			return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
		} catch (Exception e) {
			logger.error("Error while deleting company with ID {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
		}
	}

}

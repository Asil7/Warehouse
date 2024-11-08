package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dto.company.CompanyDto;
import com.example.demo.entity.Company;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.CompanyRepository;

@Service
public class CompanyService {

	private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

	@Autowired
	CompanyRepository companyRepository;

	public ApiResponse createCompany(CompanyDto companyDto) {
		try {
			if (companyRepository.existsByName(companyDto.getName())) {
				logger.warn("Company with name '{}' already exists.", companyDto.getName());
				return new ApiResponse("Company with the name " + companyDto.getName() + " already exists.", false);
			}

			Company company = new Company();
			company.setName(companyDto.getName());
			company.setPhone(companyDto.getPhone());
			company.setAdditionalPhone(companyDto.getAdditionalPhone());
			company.setUsername(companyDto.getUsername());
			company.setDescription(companyDto.getDescription());
			company.setLocation(companyDto.getLocation());
			company.setLocationMap(companyDto.getLocationMap());

			companyRepository.save(company);

			logger.info("Company created successfully: {}", companyDto.getName());
			return new ApiResponse("Company created", true);
		} catch (Exception e) {
			logger.error("Error creating company: {}", e.getMessage(), e);
			return new ApiResponse("Error creating company", false);
		}
	}

	public ApiResponse updateCompany(Long id, CompanyDto companyDto) {
		try {
			Optional<Company> optionalCompany = companyRepository.findById(id);
			if (optionalCompany.isPresent()) {
				Company existingCompany = optionalCompany.get();

				if (companyRepository.existsByNameAndIdNot(companyDto.getName(), id)) {
					logger.warn("Attempted to update company with a duplicate name: {}", companyDto.getName());
					return new ApiResponse("Company with the name " + companyDto.getName() + " already exists.", false);
				}

				existingCompany.setName(companyDto.getName());
				existingCompany.setPhone(companyDto.getPhone());
				existingCompany.setAdditionalPhone(companyDto.getAdditionalPhone());
				existingCompany.setUsername(companyDto.getUsername());
				existingCompany.setDescription(companyDto.getDescription());
				existingCompany.setLocation(companyDto.getLocation());
				existingCompany.setLocationMap(companyDto.getLocationMap());

				companyRepository.save(existingCompany);
				logger.info("Company updated successfully: {}", companyDto.getName());
				return new ApiResponse("Company updated", true);
			} else {
				logger.warn("Company with ID {} not found", id);
				return new ApiResponse("Company not found", false);
			}
		} catch (Exception e) {
			logger.error("Error updating company with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error updating company", false);
		}
	}

	public ApiResponse getAllCompany() {
		try {
			List<Company> companyList = companyRepository.findAll();
			logger.info("Retrieved {} companies", companyList.size());
			return new ApiResponse("Company List", true, companyList);
		} catch (Exception e) {
			logger.error("Error fetching company list: {}", e.getMessage(), e);
			return new ApiResponse("Error fetching company list", false);
		}
	}

	public ApiResponse getCompanyById(Long id) {
		try {
			Optional<Company> companyById = companyRepository.findById(id);
			if (companyById.isPresent()) {
				logger.info("Get company with ID {}", id);
				return new ApiResponse("Company By Id", true, companyById);
			} else {
				logger.warn("Company with ID {} not found", id);
				return new ApiResponse("Company not found", false);
			}
		} catch (Exception e) {
			logger.error("Error get company with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error get company", false);
		}
	}

	public ApiResponse deleteCompany(Long id) {
		try {
			Optional<Company> optionalCompany = companyRepository.findById(id);
			if (optionalCompany.isPresent()) {
				Company company = optionalCompany.get();
				companyRepository.delete(company);
				logger.info("Company with ID {} deleted successfully", id);
				return new ApiResponse("Company deleted", true);
			} else {
				logger.warn("Company with ID {} not found", id);
				return new ApiResponse("Company not found", false);
			}
		} catch (Exception e) {
			logger.error("Error deleting company with ID {}: {}", id, e.getMessage(), e);
			return new ApiResponse("Error deleting company", false);
		}
	}
}

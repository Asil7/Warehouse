package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.company.CompanyDto;
import com.example.demo.entity.Company;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	public ApiResponse createCompany(CompanyDto companyDto) {

		if (companyRepository.existsByName(companyDto.getName())) {
			return new ApiResponse("Company with the name " + companyDto.getName() + " already exists.", false);
		}

		Company company = new Company();
		company.setName(companyDto.getName());
		company.setPhone(companyDto.getPhone());
		company.setAdditionalPhone(companyDto.getAdditionalPhone());
		company.setDescription(companyDto.getDescription());
		company.setLocation(companyDto.getLocation());
		company.setLocationMap(companyDto.getLocationMap());

		companyRepository.save(company);

		return new ApiResponse("Company created", true);
	}

	public ApiResponse updateCompany(Long id, CompanyDto companyDto) {

		Optional<Company> optionalCompany = companyRepository.findById(id);

		if (optionalCompany.isPresent()) {
			Company existingCompany = optionalCompany.get();

			if (companyRepository.existsByNameAndIdNot(companyDto.getName(), id)) {
				return new ApiResponse("Company with the name " + companyDto.getName() + " already exists.", false);
			}
			existingCompany.setName(companyDto.getName());
			existingCompany.setPhone(companyDto.getPhone());
			existingCompany.setAdditionalPhone(companyDto.getAdditionalPhone());
			existingCompany.setDescription(companyDto.getDescription());
			existingCompany.setLocation(companyDto.getLocation());
			existingCompany.setLocationMap(companyDto.getLocationMap());

			companyRepository.save(existingCompany);
			return new ApiResponse("Company updated", true);
		} else {
			return new ApiResponse("Company not found", false);
		}
	}

	public ApiResponse getAllCompany() {
		List<Company> companyList = companyRepository.findAll();
		return new ApiResponse("Compant List", true, companyList);
	}

	public ApiResponse getCompanyById(Long id) {
		Optional<Company> companyById = companyRepository.findById(id);
		if (companyById.isPresent()) {
			return new ApiResponse("Company By Id", true, companyById);
		} else {
			return new ApiResponse("Company not found", false);
		}
	}

	public ApiResponse deleteCompany(Long id) {
		Optional<Company> optionalCompany = companyRepository.findById(id);
		if (optionalCompany.isPresent()) {
			Company company = optionalCompany.get();
			companyRepository.delete(company);
			return new ApiResponse("Company deleted", true);
		} else {
			return new ApiResponse("Company not found", false);
		}
	}

}

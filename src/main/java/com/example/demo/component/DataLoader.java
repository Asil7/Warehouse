package com.example.demo.component;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.PermissionEnums;
import com.example.demo.entity.enums.Status;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.AppConstants;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PermissionRepository permissionRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${spring.sql.init.mode}")
	private String mode;

	@Override
	public void run(String... args) throws Exception {

		if(mode.equals("always")) {
			for (PermissionEnums permissionEnum : PermissionEnums.values()) {
				Permission permission = new Permission();
				permission.setName(permissionEnum.toString());
				permissionRepository.save(permission);
			}
			
			List<Permission> allPermissions = permissionRepository.findAll();
			
			Role role = new Role();
			role.setName(AppConstants.ADMIN);
			role.setPermissions(allPermissions);
			roleRepository.save(role);
			
			User user = new User();
			user.setFullName("Jamshid");
			user.setUsername("jamshid");
			user.setPassword(passwordEncoder.encode("1221"));;
			user.setRole(role);
			user.setStatus(Status.ACTIVE);
			user.setSalary(6000000.0);
			user.setDateOfEmployment(LocalDate.of(2024, 10, 01));
			user.setPhone("+998 97 768 23 22");
			userRepository.save(user);
		}
	}
}

package com.example.demo.component;

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
				permission.setAssigned(false);
				permissionRepository.save(permission);
			}
			
			List<Permission> allPermissions = permissionRepository.findAll();
			
			Role role = new Role();
			role.setName(AppConstants.ADMIN);
			role.setPermissions(allPermissions);
			roleRepository.save(role);
			
			User user = new User();
			user.setFullName("Asilbek Fayzullayev");
			user.setUsername("asilbek");
			user.setPassword(passwordEncoder.encode("asil.777"));;
			user.setRole(role);
			user.setStatus(Status.ACTIVE);
			userRepository.save(user);
		}
	}
}

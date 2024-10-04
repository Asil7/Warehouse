package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RoleDto;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;

import jakarta.validation.Valid;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    public ApiResponse createRole(@Valid RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            return new ApiResponse("Role with the name " + roleDto.getName() + " already exists.", false);
        }

        List<Permission> permissions = permissionRepository.findAllById(roleDto.getPermissionIds());

        if (permissions.size() != roleDto.getPermissionIds().size()) {
            return new ApiResponse("One or more permissions do not exist.", false);
        }

        Role role = new Role();
        role.setName(roleDto.getName());
        role.setPermissions(permissions);
        role.setDescription(roleDto.getDescription());
        roleRepository.save(role);

        return new ApiResponse("Role successfully created", true);
    }
}

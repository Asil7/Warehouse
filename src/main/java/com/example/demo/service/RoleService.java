package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.role.EditRoleDto;
import com.example.demo.dto.role.RoleDto;
import com.example.demo.dto.role.RoleProjection;
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

    public ApiResponse editRole(@Valid Long id, EditRoleDto editRoleDto) {
        if (roleRepository.existsByName(editRoleDto.getName())) {
            return new ApiResponse("Role with the name " + editRoleDto.getName() + " already exists.", false);
        }

        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();

            role.setName(editRoleDto.getName());
            role.setDescription(editRoleDto.getDescription());
            roleRepository.save(role);

            return new ApiResponse("Role successfully updated", true);
        } else {
            return new ApiResponse("Role not found", false);
        }
    }
    
    public ApiResponse getRoleList() {
        List<RoleProjection> roleList = roleRepository.getAllRoles();
        return new ApiResponse("Role List", true, roleList);
    }

    public ApiResponse getRolePermissions(Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            List<Permission> permissions = role.getPermissions();

            return new ApiResponse("Role permissions", true, permissions);
        } else {
            return new ApiResponse("Role not found", false);
        }
    }

    public ApiResponse getPermissionsNotInRole(Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();

            List<Permission> allPermissions = permissionRepository.findAll();

            List<Permission> rolePermissions = role.getPermissions();

            List<Permission> permissionsNotInRole = allPermissions.stream()
                .filter(permission -> !rolePermissions.contains(permission))
                .collect(Collectors.toList());

            return new ApiResponse("Permissions not in the role", true, permissionsNotInRole);
        } else {
            return new ApiResponse("Role not found", false);
        }
    }
}

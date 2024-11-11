package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.permission.PermissionDto;
import com.example.demo.entity.Permission;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    public ApiResponse createPermission(@Valid PermissionDto permissionDto) {

        try {
            if (permissionRepository.existsByName(permissionDto.getName())) {
                logger.warn("Permission with the name {} already exists", permissionDto.getName());
                return new ApiResponse("Permission with the name " + permissionDto.getName() + " already exists.", false);
            }

            Permission permission = new Permission();
            permission.setName(permissionDto.getName());
            permission.setDescription(permissionDto.getDescription());
            permissionRepository.save(permission);

            logger.info("Permission created with name: {}", permissionDto.getName());
            return new ApiResponse("Permission successfully created", true);

        } catch (Exception e) {
            logger.error("Error creating permission: {}", e.getMessage(), e);
            return new ApiResponse("Error creating permission", false);
        }
    }

    public ApiResponse editPermission(@Valid Long id, PermissionDto permissionDto) {

        try {
            Optional<Permission> permissionOptional = permissionRepository.findById(id);

            if (permissionOptional.isEmpty()) {
                logger.warn("Permission not found with ID: {}", id);
                return new ApiResponse("Permission not found", false);
            }

            Permission existingPermission = permissionOptional.get();

            if (permissionRepository.existsByNameAndIdNot(permissionDto.getName(), id)) {
                logger.warn("Permission with the name {} already exists for a different ID", permissionDto.getName());
                return new ApiResponse("Permission with the name " + permissionDto.getName() + " already exists.", false);
            }

            existingPermission.setName(permissionDto.getName());
            existingPermission.setDescription(permissionDto.getDescription());
            permissionRepository.save(existingPermission);

            logger.info("Permission updated with ID: {}", id);
            return new ApiResponse("Permission updated", true);

        } catch (Exception e) {
            logger.error("Error editing permission with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error editing permission", false);
        }
    }

    public ApiResponse getAllPermissions() {

        try {
            List<Permission> permissionList = permissionRepository.findAll();
            logger.info("Fetched {} permissions", permissionList.size());
            return new ApiResponse("Permission List", true, permissionList);

        } catch (Exception e) {
            logger.error("Error fetching permissions: {}", e.getMessage(), e);
            return new ApiResponse("Error fetching permissions", false);
        }
    }

    public ApiResponse deletePermission(Long id) {

        try {
            Optional<Permission> permissionOptional = permissionRepository.findById(id);

            if (permissionOptional.isEmpty()) {
                logger.warn("Permission not found with ID: {}", id);
                return new ApiResponse("Permission not found", false);
            }

            Permission permission = permissionOptional.get();

            boolean isPermissionInUse = roleRepository.existsByPermissionsContaining(permission);

            if (isPermissionInUse) {
                logger.warn("Permission with ID {} cannot be deleted as it is assigned to a role", id);
                return new ApiResponse("Permission cannot be deleted as it is assigned to role.", false);
            }

            permissionRepository.delete(permission);
            logger.info("Permission deleted with ID: {}", id);
            return new ApiResponse("Permission deleted", true);

        } catch (Exception e) {
            logger.error("Error deleting permission with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse("Error deleting permission", false);
        }
    }
}

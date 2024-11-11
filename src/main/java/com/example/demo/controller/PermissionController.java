package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.permission.PermissionDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.PermissionService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    PermissionService permissionService;

    @PreAuthorize("hasAuthority('ADD_PERMISSION')")
    @PostMapping
    public HttpEntity<?> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        try {
            ApiResponse apiResponse = permissionService.createPermission(permissionDto);
            logger.info("Permission created successfully.");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error creating permission: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while creating the permission", false));
        }
    }

//    @PreAuthorize("hasAuthority('EDIT_PERMISSION')")
    @PutMapping("/{id}")
    public HttpEntity<?> editPermission(@Valid @PathVariable Long id, @RequestBody PermissionDto permissionDto) {
        try {
            ApiResponse apiResponse = permissionService.editPermission(id, permissionDto);
            logger.info("Permission edited successfully for ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error editing permission for ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while editing the permission", false));
        }
    }

    @PreAuthorize("hasAuthority('VIEW_PERMISSION_LIST')")
    @GetMapping
    public HttpEntity<?> getPermissionList() {
        try {
            ApiResponse apiResponse = permissionService.getAllPermissions();
            logger.info("Retrieved permission list.");
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error retrieving permission list: ", e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while retrieving permissions", false));
        }
    }

//    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deletePermission(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = permissionService.deletePermission(id);
            logger.info("Permission deleted successfully for ID: {}", id);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (Exception e) {
            logger.error("Error deleting permission for ID {}: ", id, e);
            return ResponseEntity.status(500).body(new ApiResponse("An error occurred while deleting the permission", false));
        }
    }
}

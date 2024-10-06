package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.role.RoleProjection;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	boolean existsByName(String name);

	boolean existsByPermissionsContaining(Permission permission);

	@Query(value = "SELECT r.id AS id, r.name AS name, r.description AS description, r.created_at AS createdAt " +
    "FROM roles r",
    nativeQuery = true)
    List<RoleProjection> getAllRoles();

}

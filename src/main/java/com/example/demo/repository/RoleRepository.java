package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.role.RoleProjection;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	boolean existsByName(String name);

	boolean existsByPermissionsContaining(Permission permission);
	
	boolean existsByNameAndIdNot(String name, Long id);

	@Query(value = "SELECT r.id AS id, r.name AS name, r.description AS description, r.created_at AS createdAt " +
    "FROM roles r",
    nativeQuery = true)
    List<RoleProjection> getAllRoles();

    @Query(value = "SELECT r.id AS id, r.name AS name, r.description AS description, r.created_at AS createdAt " +
    "FROM roles r WHERE r.id = :id", 
    nativeQuery = true)
    Optional<RoleProjection> getRoleById(Long id);
}

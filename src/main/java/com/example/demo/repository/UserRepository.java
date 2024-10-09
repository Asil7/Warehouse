package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.user.UserProjection;
import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);

	@Query(value = "SELECT u.id AS id, u.full_name AS fullName, u.username AS username, u.status AS status, r.name AS roleName, u.created_at AS createdAt "
			+ "FROM users u " + "JOIN roles r ON u.role_id = r.id", nativeQuery = true)
	List<UserProjection> getAllUsers();

	@Query(value = "SELECT u.id AS id, u.full_name AS fullName, u.username AS username, u.status AS status, r.name AS roleName, u.created_at AS createdAt "
			+ "FROM users u " + "JOIN roles r ON u.role_id = r.id " + "WHERE u.id = :id", nativeQuery = true)
	Optional<UserProjection> getUserById(Long id);

}

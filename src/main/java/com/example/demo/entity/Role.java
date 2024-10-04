package com.example.demo.entity;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.entity.template.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class Role extends AbstractEntity {

	@Column(unique = true, nullable = false)
    private String name;

	@Enumerated(value = EnumType.STRING)
    @ManyToMany
    private List<Permission> permissions;
	
	@Column(length = 500)
	private String description;

    public List<GrantedAuthority> getAuthorities() {
        return permissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());
    }
}

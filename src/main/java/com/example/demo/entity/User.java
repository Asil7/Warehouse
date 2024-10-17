package com.example.demo.entity;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.enums.Status;
import com.example.demo.entity.template.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbstractEntity implements UserDetails{

	@Column(nullable = false)
	private String fullName;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	private String phone;

	@Column(nullable = false)
	private String password;
	
	@ManyToOne(optional = false)
	private Role role;
	
	@Column(nullable = false)
	private Double salary;

	private LocalDate dateOfEmployment;

	@Enumerated(value = EnumType.STRING)
	private Status status;

	@Column(length = 1024)
	private String token;
	

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}
	
	@Override
    public boolean isEnabled() {
        return this.status == Status.ACTIVE;
    }
	
}

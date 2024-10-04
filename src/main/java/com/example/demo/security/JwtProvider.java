package com.example.demo.security;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	
	private static final long expireTime = 1000 * 60 * 60 * 24;
	private static final String key = "warehouse";

	public String generateToken(String username, Set<Role> roles) {
		Date expireDate = new Date(System.currentTimeMillis() + expireTime);
		String token =  Jwts
			.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(expireDate)
			.claim("roles", roles)
			.signWith(SignatureAlgorithm.HS512, key)
			.compact();
		return token;
	}
}

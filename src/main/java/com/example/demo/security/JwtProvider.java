package com.example.demo.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private static final long expireTime = 1000 * 60 * 60 * 24;
	private static final String key = "warehouse";

public String generateToken(String username, Role role) {
    Date expireDate = new Date(System.currentTimeMillis() + expireTime);
    
    List<String> permissions = role.getPermissions().stream()
        .map(Permission::getName)
        .collect(Collectors.toList());

    String token = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .claim("role", role.getName()) 
        .claim("permissions", permissions)
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();

    return token;
}

	public String getUserNameFromToken(String token) {
		try {
			String username = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
			return username;
		} catch (Exception e) {
			return null;
		}
	}

}

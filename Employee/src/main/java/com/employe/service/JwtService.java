package com.employe.service;

import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtService {
	private static String Key = "345ev4sf094rjdn49ru49oifdsnf4e49tr8e9jvonfv0ew9eur04uoijd3049";

	public String extractEmployeeId(String token) {
		
		return extractClaims(token).getSubject();
	}

	public static Key getSignKey() {
		// TODO Auto-generated method stub
		byte[] keybytes = Decoders.BASE64.decode(Key);
		return Keys.hmacShaKeyFor(keybytes);
	}

	public Claims extractClaims(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		return claims;
	}
}

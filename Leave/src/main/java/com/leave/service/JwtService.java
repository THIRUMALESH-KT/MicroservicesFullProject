package com.leave.service;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtService {
	private static String Key = "vavavavavasesfsfasdfsvandfdfndskfndsfnamcsfksnDfdfmsd";

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
